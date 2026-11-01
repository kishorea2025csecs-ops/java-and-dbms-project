package com.parkease.service;

import com.parkease.constant.PaymentMethod;
import com.parkease.constant.PaymentStatus;
import com.parkease.constant.SlotStatus;
import com.parkease.constant.TicketStatus;
import com.parkease.dto.*;
import com.parkease.entity.*;
import com.parkease.exception.*;
import com.parkease.model.*;
import com.parkease.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Core Parking Management Service.
 * Fully synchronized with MySQL 8 parkease_db schema.
 */
@Service
public class ParkingService {

    private final ParkingSlotRepository slotRepository;
    private final ParkingTicketRepository ticketRepository;
    private final VehicleRepository vehicleRepository;
    private final PaymentRepository paymentRepository;
    private final VehicleRateRepository rateRepository;
    private final VehicleService vehicleService;

    @Autowired
    public ParkingService(
            ParkingSlotRepository slotRepository,
            ParkingTicketRepository ticketRepository,
            VehicleRepository vehicleRepository,
            PaymentRepository paymentRepository,
            VehicleRateRepository rateRepository,
            VehicleService vehicleService) {
        this.slotRepository = slotRepository;
        this.ticketRepository = ticketRepository;
        this.vehicleRepository = vehicleRepository;
        this.paymentRepository = paymentRepository;
        this.rateRepository = rateRepository;
        this.vehicleService = vehicleService;
    }

    /**
     * Allocates parking slots and generates an active parking ticket.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public synchronized ParkingResponse parkVehicle(ParkVehicleRequest request) {
        String cleanVehicleNumber = request.getVehicleNumber().toUpperCase().trim();

        // 1. Check whether vehicle is already parked with an ACTIVE ticket
        Optional<ParkingTicket> activeTicket = ticketRepository.findActiveTicketByVehicleNumber(cleanVehicleNumber);
        if (activeTicket.isPresent()) {
            throw new DuplicateVehicleException("Vehicle " + cleanVehicleNumber + 
                    " is already parked under Ticket #" + activeTicket.get().getTicketNumber() + "!");
        }

        // 2. Fetch vehicle rate & requirements
        VehicleType type = request.getVehicleType();
        int slotsNeeded = type.getSlotsRequired();
        double hourlyRate = getHourlyRateForType(type);

        // 3. Find available slot(s)
        List<ParkingSlot> allSlots = slotRepository.findAllByOrderBySlotIdAsc();
        List<ParkingSlot> allocatedSlots = findAvailableSlotsForType(allSlots, type);

        if (allocatedSlots.isEmpty() || allocatedSlots.size() < slotsNeeded) {
            if (type == VehicleType.TRUCK) {
                throw new ParkingFullException("Cannot park Truck: No 2 consecutive parking slots available!");
            } else {
                throw new ParkingFullException("Parking lot is full! No available slot for " + type.getDisplayName());
            }
        }

        // 4. Save or retrieve vehicle entity
        VehicleEntity vehicleEntity = vehicleService.findOrCreateVehicle(
                request.getOwnerName(),
                cleanVehicleNumber,
                type,
                request.getContactNumber()
        );

        // 5. Update slot statuses in database
        String slotNumbersStr = allocatedSlots.stream()
                .map(ParkingSlot::getSlotNumber)
                .collect(Collectors.joining(", "));

        for (ParkingSlot slot : allocatedSlots) {
            slot.setStatus(SlotStatus.OCCUPIED);
            slot.setCurrentVehicle(vehicleEntity);
            slotRepository.save(slot);
        }

        // 6. Generate unique Ticket Number
        LocalDateTime entryTime = LocalDateTime.now();
        if (request.getCustomEntryTime() != null && !request.getCustomEntryTime().trim().isEmpty()) {
            try {
                entryTime = LocalDateTime.parse(request.getCustomEntryTime());
            } catch (Exception ignored) {}
        }
        String ticketNumber = generateUniqueTicketNumber();

        // 7. Save Parking Ticket
        ParkingTicket ticket = new ParkingTicket(ticketNumber, vehicleEntity, entryTime, slotNumbersStr);
        ticket.setHourlyRate(BigDecimal.valueOf(hourlyRate));
        ticketRepository.save(ticket);

        // 8. Build success response
        ParkingResponse response = new ParkingResponse(true, "Vehicle parked successfully!");
        response.setTicketNumber(ticketNumber);
        response.setVehicleNumber(vehicleEntity.getVehicleNumber());
        response.setOwnerName(vehicleEntity.getOwnerName());
        response.setVehicleType(type.getDisplayName());
        response.setAllocatedSlots(slotNumbersStr);
        response.setEntryTime(entryTime);
        response.setHourlyRate(BigDecimal.valueOf(hourlyRate));

        return response;
    }

    /**
     * Algorithm for finding required slots:
     * - BIKE/CAR: First available slot
     * - TRUCK: First two consecutive available slots
     */
    private List<ParkingSlot> findAvailableSlotsForType(List<ParkingSlot> slots, VehicleType type) {
        List<ParkingSlot> result = new ArrayList<>();

        if (type == VehicleType.TRUCK) {
            for (int i = 0; i < slots.size() - 1; i++) {
                ParkingSlot current = slots.get(i);
                ParkingSlot next = slots.get(i + 1);

                if (current.getStatus() == SlotStatus.AVAILABLE &&
                    next.getStatus() == SlotStatus.AVAILABLE &&
                    next.getNumericSlotNumber() == current.getNumericSlotNumber() + 1) {
                    result.add(current);
                    result.add(next);
                    return result;
                }
            }
        } else {
            for (ParkingSlot slot : slots) {
                if (slot.getStatus() == SlotStatus.AVAILABLE) {
                    result.add(slot);
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * Searches for an active ticket by Ticket Number OR Vehicle Registration Number.
     */
    @Transactional(readOnly = true)
    public ExitCalculationDto calculateExitCharge(String searchKey) {
        if (searchKey == null || searchKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter a Ticket ID or Vehicle Registration Number.");
        }
        String key = searchKey.trim().toUpperCase();

        ParkingTicket ticket = ticketRepository.findByTicketNumberIgnoreCase(key)
                .orElseGet(() -> ticketRepository.findActiveTicketByVehicleNumber(key)
                        .orElseThrow(() -> new TicketNotFoundException("No active parking ticket found for: " + key)));

        if (ticket.getTicketStatus() != TicketStatus.ACTIVE) {
            throw new InvalidTicketStateException("Ticket #" + ticket.getTicketNumber() + 
                    " is already " + ticket.getTicketStatus().getDisplayName() + ". Cannot process exit again!");
        }

        LocalDateTime exitTime = LocalDateTime.now();
        return computeExitDto(ticket, exitTime);
    }

    public ExitCalculationDto computeExitDto(ParkingTicket ticket, LocalDateTime exitTime) {
        VehicleEntity vehicleEntity = ticket.getVehicle();
        VehicleType type = vehicleEntity.getVehicleType();
        double hourlyRate = getHourlyRateForType(type);

        Vehicle oopVehicle = vehicleService.createOopVehicle(vehicleEntity, hourlyRate);

        long totalMinutes = Math.max(1, Duration.between(ticket.getEntryTime(), exitTime).toMinutes());
        int rawCeilingHours = (int) Math.ceil((double) totalMinutes / 60.0);
        if (rawCeilingHours <= 0) rawCeilingHours = 1;

        int billedHours = Math.min(rawCeilingHours, Vehicle.MAX_BILLING_HOURS);
        boolean isCapped = rawCeilingHours > Vehicle.MAX_BILLING_HOURS;

        double finalAmount = oopVehicle.calculateCharge(rawCeilingHours);

        ExitCalculationDto dto = new ExitCalculationDto();
        dto.setTicketId(ticket.getTicketId());
        dto.setTicketNumber(ticket.getTicketNumber());
        dto.setVehicleNumber(vehicleEntity.getVehicleNumber());
        dto.setOwnerName(vehicleEntity.getOwnerName());
        dto.setContactNumber(vehicleEntity.getContactNumber());
        dto.setVehicleType(type.getDisplayName());
        dto.setAllocatedSlots(ticket.getAllocatedSlots());
        dto.setEntryTime(ticket.getEntryTime());
        dto.setExitTime(exitTime);
        dto.setTotalMinutes(totalMinutes);
        dto.setBilledHours(billedHours);
        dto.setDailyCapApplied(isCapped);
        dto.setHourlyRate(BigDecimal.valueOf(hourlyRate));
        dto.setTotalAmount(BigDecimal.valueOf(finalAmount));
        dto.setTicketStatus(ticket.getTicketStatus().name());

        return dto;
    }

    /**
     * Processes Vehicle Exit:
     * - Marks ticket COMPLETED
     * - Records payment in payments table
     * - Releases allocated parking slots (back to AVAILABLE)
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public synchronized ReceiptDto processVehicleExit(ProcessExitRequest request) {
        String ticketNumber = request.getTicketNumber().trim();
        ParkingTicket ticket = ticketRepository.findByTicketNumberIgnoreCase(ticketNumber)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found: " + ticketNumber));

        if (ticket.getTicketStatus() != TicketStatus.ACTIVE) {
            throw new InvalidTicketStateException("Ticket #" + ticketNumber + 
                    " is already processed (" + ticket.getTicketStatus() + "). Cannot complete exit again!");
        }

        if (paymentRepository.findByTicket_TicketId(ticket.getTicketId()).isPresent()) {
            throw new DuplicatePaymentException("Payment has already been recorded for this ticket!");
        }

        LocalDateTime exitTime = LocalDateTime.now();
        ExitCalculationDto calc = computeExitDto(ticket, exitTime);

        BigDecimal amount = (request.getAmountPaid() != null && request.getAmountPaid().compareTo(BigDecimal.ZERO) > 0)
                ? request.getAmountPaid()
                : calc.getTotalAmount();

        // 1. Update Ticket
        ticket.setExitTime(exitTime);
        ticket.setParkingHours(calc.getBilledHours());
        ticket.setTotalAmount(amount);
        ticket.setTicketStatus(TicketStatus.COMPLETED);
        ticketRepository.save(ticket);

        // 2. Record Payment
        PaymentMethod method = request.getPaymentMethod() != null ? request.getPaymentMethod() : PaymentMethod.CASH;
        Payment payment = new Payment(ticket, amount, method, PaymentStatus.PAID);
        payment = paymentRepository.save(payment);

        // 3. Release Allocated Slots
        releaseSlots(ticket.getAllocatedSlots());

        // 4. Return Receipt DTO
        ReceiptDto receipt = new ReceiptDto();
        receipt.setReceiptNumber(payment.getTransactionRef() != null ? payment.getTransactionRef() : "REC-" + payment.getPaymentId());
        receipt.setTicketNumber(ticket.getTicketNumber());
        receipt.setVehicleNumber(ticket.getVehicle().getVehicleNumber());
        receipt.setOwnerName(ticket.getVehicle().getOwnerName());
        receipt.setContactNumber(ticket.getVehicle().getContactNumber());
        receipt.setVehicleType(ticket.getVehicle().getVehicleType().getDisplayName());
        receipt.setAllocatedSlots(ticket.getAllocatedSlots());
        receipt.setEntryTime(ticket.getEntryTime());
        receipt.setExitTime(exitTime);
        receipt.setDurationMinutes(calc.getTotalMinutes());
        receipt.setBilledHours(calc.getBilledHours());
        receipt.setHourlyRate(calc.getHourlyRate());
        receipt.setTotalAmount(amount);
        receipt.setPaymentMethod(method.getDescription());
        receipt.setPaymentStatus(PaymentStatus.PAID.getDisplayName());
        receipt.setPaymentTime(payment.getPaymentTime());

        return receipt;
    }

    /**
     * Releases slots back to AVAILABLE status.
     * Handles formats like "SLOT-01", "SLOT-07, SLOT-08", or "1, 2".
     */
    private void releaseSlots(String slotString) {
        if (slotString == null || slotString.trim().isEmpty()) return;

        String[] parts = slotString.split(",");
        for (String part : parts) {
            String token = part.trim();
            // Try direct match first (e.g. "SLOT-01")
            Optional<ParkingSlot> opt = slotRepository.findBySlotNumberIgnoreCase(token);
            if (opt.isEmpty()) {
                // Try numeric formatting
                try {
                    int num = Integer.parseInt(token.replaceAll("[^0-9]", ""));
                    opt = slotRepository.findBySlotNumberIgnoreCase(String.format("SLOT-%02d", num));
                } catch (Exception ignored) {}
            }

            opt.ifPresent(slot -> {
                slot.setStatus(SlotStatus.AVAILABLE);
                slot.setCurrentVehicle(null);
                slotRepository.save(slot);
            });
        }
    }

    public List<ParkingSlot> getAllSlots() {
        return slotRepository.findAllByOrderBySlotIdAsc();
    }

    public List<ParkingTicket> getAllTickets() {
        return ticketRepository.findAllOrderByEntryTimeDesc();
    }

    public List<ParkingTicket> getActiveTickets() {
        return ticketRepository.findByTicketStatusOrderByEntryTimeDesc(TicketStatus.ACTIVE);
    }

    public List<ParkingTicket> searchTickets(String keyword, TicketStatus status, VehicleType type) {
        return ticketRepository.searchTickets(
                keyword != null && !keyword.trim().isEmpty() ? keyword.trim() : null,
                status,
                type
        );
    }

    public double getHourlyRateForType(VehicleType type) {
        return rateRepository.findByVehicleType(type)
                .map(r -> r.getHourlyRate().doubleValue())
                .orElse(type.getDefaultHourlyRate());
    }

    private String generateUniqueTicketNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int randomNum = 1000 + new Random().nextInt(9000);
        return "TKT-" + datePart + "-" + randomNum;
    }
}
