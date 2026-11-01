package com.parkease.model;

/**
 * Child Class Bike extending Vehicle.
 * Demonstrates Inheritance, Polymorphism, and Method Overriding.
 */
public class Bike extends Vehicle {

    public static final double DEFAULT_HOURLY_RATE = 10.00;
    public static final int SLOTS_REQUIRED = 1;

    private double customRate = DEFAULT_HOURLY_RATE;

    public Bike(String vehicleNumber, String ownerName, String contactNumber) {
        super(vehicleNumber, ownerName, VehicleType.BIKE, contactNumber);
    }

    public Bike(String vehicleNumber, String ownerName, String contactNumber, double customRate) {
        super(vehicleNumber, ownerName, VehicleType.BIKE, contactNumber);
        if (customRate > 0) {
            this.customRate = customRate;
        }
    }

    @Override
    public double getHourlyRate() {
        return customRate;
    }

    @Override
    public int getRequiredSlots() {
        return SLOTS_REQUIRED;
    }

    @Override
    public String toString() {
        return "Bike [Reg: " + getVehicleNumber() + ", Owner: " + getOwnerName() + 
               ", Rate: ₹" + getHourlyRate() + "/hr, Slots: " + getRequiredSlots() + "]";
    }
}

