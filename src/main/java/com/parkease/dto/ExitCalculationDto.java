package com.parkease.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExitCalculationDto {

    private Long ticketId;
    private String ticketNumber;
    private String vehicleNumber;
    private String ownerName;
    private String contactNumber;
    private String vehicleType;
    private String allocatedSlots;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private long totalMinutes;
    private int billedHours;
    private boolean dailyCapApplied;
    private BigDecimal hourlyRate;
    private BigDecimal totalAmount;
    private String ticketStatus;

    public ExitCalculationDto() {}

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

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getAllocatedSlots() {
        return allocatedSlots;
    }

    public void setAllocatedSlots(String allocatedSlots) {
        this.allocatedSlots = allocatedSlots;
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

    public long getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(long totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public int getBilledHours() {
        return billedHours;
    }

    public void setBilledHours(int billedHours) {
        this.billedHours = billedHours;
    }

    public boolean isDailyCapApplied() {
        return dailyCapApplied;
    }

    public void setDailyCapApplied(boolean dailyCapApplied) {
        this.dailyCapApplied = dailyCapApplied;
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

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getFormattedDuration() {
        long hours = totalMinutes / 60;
        long mins = totalMinutes % 60;
        return String.format("%d hr %d min", hours, mins);
    }
}

