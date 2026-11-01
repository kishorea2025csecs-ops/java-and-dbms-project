package com.parkease.service;

import com.parkease.constant.SlotStatus;
import com.parkease.constant.TicketStatus;
import com.parkease.dto.DashboardStatsDto;
import com.parkease.entity.ParkingTicket;
import com.parkease.entity.Payment;
import com.parkease.model.VehicleType;
import com.parkease.repository.ParkingSlotRepository;
import com.parkease.repository.ParkingTicketRepository;
import com.parkease.repository.PaymentRepository;
import com.parkease.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReportService {

    private final ParkingSlotRepository slotRepository;
    private final ParkingTicketRepository ticketRepository;
    private final VehicleRepository vehicleRepository;
    private final PaymentRepository paymentRepository;
    private final BillingService billingService;

    @Autowired
    public ReportService(
            ParkingSlotRepository slotRepository,
            ParkingTicketRepository ticketRepository,
            VehicleRepository vehicleRepository,
            PaymentRepository paymentRepository,
            BillingService billingService) {
        this.slotRepository = slotRepository;
        this.ticketRepository = ticketRepository;
        this.vehicleRepository = vehicleRepository;
        this.paymentRepository = paymentRepository;
        this.billingService = billingService;
    }

    /**
     * Aggregates real-time Dashboard statistics directly from MySQL database.
     */
    public DashboardStatsDto getDashboardStatistics() {
        DashboardStatsDto stats = new DashboardStatsDto();

        long occupied = slotRepository.countByStatus(SlotStatus.OCCUPIED);
        long available = slotRepository.countByStatus(SlotStatus.AVAILABLE);
        long reserved = slotRepository.countByStatus(SlotStatus.RESERVED);

        stats.setTotalSlots(20);
        stats.setOccupiedSlots(occupied);
        stats.setAvailableSlots(available);
        stats.setReservedSlots(reserved);

        double occupancyPct = stats.getTotalSlots() > 0 ? ((double) occupied / stats.getTotalSlots()) * 100.0 : 0.0;
        stats.setOccupancyPercentage(Math.round(occupancyPct * 10.0) / 10.0);

        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime endOfToday = LocalDate.now().atTime(LocalTime.MAX);
        long parkedToday = ticketRepository.countTicketsBetween(startOfToday, endOfToday);
        stats.setVehiclesParkedToday(parkedToday);

        BigDecimal revenueToday = billingService.getTodayRevenue();
        stats.setRevenueCollectedToday(revenueToday != null ? revenueToday : BigDecimal.ZERO);

        long activeTickets = ticketRepository.countByTicketStatus(TicketStatus.ACTIVE);
        stats.setActiveTickets(activeTickets);

        // Vehicle category breakdown
        Map<String, Long> categoryMap = new LinkedHashMap<>();
        for (VehicleType vt : VehicleType.values()) {
            categoryMap.put(vt.name(), vehicleRepository.countByVehicleType(vt));
        }
        stats.setVehicleCategoryDistribution(categoryMap);

        // Daily revenue for last 7 days
        Map<String, BigDecimal> last7DaysRevenue = new LinkedHashMap<>();
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd MMM");
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime dayStart = date.atStartOfDay();
            LocalDateTime dayEnd = date.atTime(LocalTime.MAX);
            BigDecimal dayRev = paymentRepository.calculateRevenueBetween(dayStart, dayEnd);
            last7DaysRevenue.put(date.format(dayFormatter), dayRev != null ? dayRev : BigDecimal.ZERO);
        }
        stats.setDailyRevenueLast7Days(last7DaysRevenue);

        return stats;
    }

    /**
     * Reports summary metrics with date filter
     */
    public Map<String, Object> getAnalyticsReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();

        if (startDate == null) startDate = LocalDate.now().minusDays(30);
        if (endDate == null) endDate = LocalDate.now();

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<ParkingTicket> tickets = ticketRepository.findByDateRange(start, end);
        BigDecimal totalRevenue = paymentRepository.calculateRevenueBetween(start, end);

        long totalParked = tickets.size();
        long completed = tickets.stream().filter(t -> t.getTicketStatus() == TicketStatus.COMPLETED).count();

        // Most frequent category
        Map<String, Long> typeCounts = new HashMap<>();
        for (ParkingTicket t : tickets) {
            String type = t.getVehicle().getVehicleType().name();
            typeCounts.put(type, typeCounts.getOrDefault(type, 0L) + 1);
        }

        String mostFrequent = typeCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("CAR");

        report.put("startDate", startDate);
        report.put("endDate", endDate);
        report.put("totalParked", totalParked);
        report.put("completedSessions", completed);
        report.put("totalRevenue", totalRevenue != null ? totalRevenue : BigDecimal.ZERO);
        report.put("mostFrequentType", mostFrequent);
        report.put("tickets", tickets);

        return report;
    }

    /**
     * Generates CSV content string for export.
     */
    public String generateTicketsCsv(List<ParkingTicket> tickets) {
        StringBuilder csv = new StringBuilder();
        csv.append("Ticket ID,Ticket Number,Vehicle Number,Owner Name,Vehicle Type,Allocated Slots,Entry Time,Exit Time,Hours,Amount (INR),Status\n");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (ParkingTicket t : tickets) {
            csv.append(t.getTicketId()).append(",")
               .append(escapeCsv(t.getTicketNumber())).append(",")
               .append(escapeCsv(t.getVehicle().getVehicleNumber())).append(",")
               .append(escapeCsv(t.getVehicle().getOwnerName())).append(",")
               .append(t.getVehicle().getVehicleType()).append(",")
               .append("\"").append(t.getAllocatedSlots()).append("\",")
               .append(t.getEntryTime() != null ? t.getEntryTime().format(dtf) : "").append(",")
               .append(t.getExitTime() != null ? t.getExitTime().format(dtf) : "N/A").append(",")
               .append(t.getParkingHours() != null ? t.getParkingHours() : 0).append(",")
               .append(t.getTotalAmount() != null ? t.getTotalAmount() : "0.00").append(",")
               .append(t.getTicketStatus()).append("\n");
        }

        return csv.toString();
    }

    private String escapeCsv(String val) {
        if (val == null) return "";
        return "\"" + val.replace("\"", "\"\"") + "\"";
    }
}

