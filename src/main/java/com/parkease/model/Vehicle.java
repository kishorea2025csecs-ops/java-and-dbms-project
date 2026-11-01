package com.parkease.model;

import com.parkease.exception.InvalidVehicleException;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Abstract Parent Class demonstrating Abstraction, Encapsulation, and Inheritance.
 * Serves as the base class for specialized vehicle types: Bike, Car, and Truck.
 */
public abstract class Vehicle implements Chargeable {

    // Static constant for standard currency symbol
    public static final String CURRENCY_SYMBOL = "₹";

    // Encapsulated fields
    private final String vehicleNumber;
    private String ownerName;
    private final VehicleType vehicleType;
    private String contactNumber;

    /**
     * Parameterized Constructor with validation.
     * Demonstrates Constructor Encapsulation.
     */
    public Vehicle(String vehicleNumber, String ownerName, VehicleType vehicleType, String contactNumber) {
        if (vehicleNumber == null || vehicleNumber.trim().isEmpty()) {
            throw new InvalidVehicleException("Vehicle registration number cannot be blank.");
        }
        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new InvalidVehicleException("Owner name cannot be blank.");
        }
        if (vehicleType == null) {
            throw new InvalidVehicleException("Vehicle category must be specified.");
        }
        this.vehicleNumber = vehicleNumber.trim().toUpperCase();
        this.ownerName = ownerName.trim();
        this.vehicleType = vehicleType;
        this.contactNumber = contactNumber != null ? contactNumber.trim() : "";
    }

    // Abstract methods to be overridden by subclasses (Polymorphism)
    public abstract double getHourlyRate();
    public abstract int getRequiredSlots();

    /**
     * Polymorphic implementation of Chargeable:
     * Formula: Charge = Minimum(CEILING(Parking Duration in Hours), 8) * Hourly Rate
     */
    @Override
    public double calculateCharge(int hours) {
        if (hours <= 0) {
            hours = 1; // Minimum billing unit is 1 hour
        }
        int effectiveHours = Math.min(hours, MAX_BILLING_HOURS);
        return effectiveHours * getHourlyRate();
    }

    @Override
    public double calculateCharge(long minutes) {
        if (minutes <= 0) {
            return calculateCharge(1);
        }
        // Round partial hours UP to the next complete hour: CEILING
        int hours = (int) Math.ceil((double) minutes / 60.0);
        return calculateCharge(hours);
    }

    @Override
    public double calculateCharge(LocalDateTime entryTime, LocalDateTime exitTime) {
        if (entryTime == null) {
            return calculateCharge(1);
        }
        if (exitTime == null) {
            exitTime = LocalDateTime.now();
        }
        if (exitTime.isBefore(entryTime)) {
            throw new IllegalArgumentException("Exit time cannot be earlier than entry time.");
        }
        long minutes = Duration.between(entryTime, exitTime).toMinutes();
        return calculateCharge(minutes);
    }

    // Getters and Setters (Encapsulation)
    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new InvalidVehicleException("Owner name cannot be empty.");
        }
        this.ownerName = ownerName.trim();
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber != null ? contactNumber.trim() : "";
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "registration='" + vehicleNumber + '\'' +
                ", owner='" + ownerName + '\'' +
                ", type=" + vehicleType +
                ", slots=" + getRequiredSlots() +
                ", rate=" + CURRENCY_SYMBOL + getHourlyRate() + "/hr" +
                '}';
    }
}

