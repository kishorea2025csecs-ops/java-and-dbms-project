package com.parkease.service;

import com.parkease.entity.*;
import com.parkease.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service providing full relational database inspection and metrics
 * for the DBMS Academic demonstration and viva presentation.
 */
@Service
public class DatabaseRecordService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingSlotRepository slotRepository;
    private final ParkingTicketRepository ticketRepository;
    private final PaymentRepository paymentRepository;
    private final VehicleRateRepository rateRepository;

    @Autowired
    public DatabaseRecordService(
            UserRepository userRepository,
            VehicleRepository vehicleRepository,
            ParkingSlotRepository slotRepository,
            ParkingTicketRepository ticketRepository,
            PaymentRepository paymentRepository,
            VehicleRateRepository rateRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.slotRepository = slotRepository;
        this.ticketRepository = ticketRepository;
        this.paymentRepository = paymentRepository;
        this.rateRepository = rateRepository;
    }

    public Map<String, Long> getTableCounts() {
        Map<String, Long> counts = new HashMap<>();
        counts.put("users", userRepository.count());
        counts.put("vehicles", vehicleRepository.count());
        counts.put("parking_slots", slotRepository.count());
        counts.put("parking_tickets", ticketRepository.count());
        counts.put("payments", paymentRepository.count());
        counts.put("vehicle_rates", rateRepository.count());
        return counts;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<VehicleEntity> getVehicles() {
        return vehicleRepository.findAll();
    }

    public List<ParkingSlot> getParkingSlots() {
        return slotRepository.findAllByOrderBySlotNumberAsc();
    }

    public List<ParkingTicket> getParkingTickets() {
        return ticketRepository.findAllOrderByEntryTimeDesc();
    }

    public List<Payment> getPayments() {
        return paymentRepository.findAllOrderByPaymentTimeDesc();
    }

    public List<VehicleRate> getVehicleRates() {
        return rateRepository.findAll();
    }
}

