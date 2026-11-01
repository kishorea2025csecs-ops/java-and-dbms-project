package com.parkease.entity;

import com.parkease.constant.TicketStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_tickets", indexes = {
    @Index(name = "idx_tickets_num", columnList = "ticket_number"),
    @Index(name = "idx_tickets_status", columnList = "ticket_status")
})
public class ParkingTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "ticket_number", nullable = false, unique = true, length = 50)
    private String ticketNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ticket_vehicle"))
    private VehicleEntity vehicle;

    @Column(name = "entry_time", nullable = false)
    private LocalDateTime entryTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    @Column(name = "parking_hours")
    private Integer parkingHours;

    @Column(name = "allocated_slots", nullable = false, length = 50)
    private String allocatedSlots;

    @Column(name = "hourly_rate", precision = 10, scale = 2)
    private BigDecimal hourlyRate = BigDecimal.ZERO;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_status", nullable = false, length = 20)
    private TicketStatus ticketStatus = TicketStatus.ACTIVE;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ParkingTicket() {}

    public ParkingTicket(String ticketNumber, VehicleEntity vehicle, LocalDateTime entryTime, String allocatedSlots) {
        this.ticketNumber = ticketNumber;
        this.vehicle = vehicle;
        this.entryTime = entryTime != null ? entryTime : LocalDateTime.now();
        this.allocatedSlots = allocatedSlots;
        this.ticketStatus = TicketStatus.ACTIVE;
        this.totalAmount = BigDecimal.ZERO;
        this.hourlyRate = BigDecimal.valueOf(vehicle != null && vehicle.getVehicleType() != null ? vehicle.getVehicleType().getDefaultHourlyRate() : 30.0);
        this.createdAt = LocalDateTime.now();
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public VehicleEntity getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleEntity vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public Integer getParkingHours() {
        return parkingHours;
    }

    public void setParkingHours(Integer parkingHours) {
        this.parkingHours = parkingHours;
    }

    public String getAllocatedSlots() {
        return allocatedSlots;
    }

    public void setAllocatedSlots(String allocatedSlots) {
        this.allocatedSlots = allocatedSlots;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
