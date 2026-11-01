package com.parkease.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ParkingResponse {

    private boolean success;
    private String message;
    private String ticketNumber;
    private String vehicleNumber;
    private String ownerName;
    private String vehicleType;
    private String allocatedSlots;
    private LocalDateTime entryTime;
    private BigDecimal hourlyRate;

    public ParkingResponse() {}

    public ParkingResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static ParkingResponse error(String message) {
        return new ParkingResponse(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
}

