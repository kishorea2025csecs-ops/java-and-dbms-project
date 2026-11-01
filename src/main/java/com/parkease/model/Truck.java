package com.parkease.model;

/**
 * Child Class Truck extending Vehicle.
 * Demonstrates Inheritance, Polymorphism, and Method Overriding.
 * Requires 2 consecutive parking slots.
 */
public class Truck extends Vehicle {

    public static final double DEFAULT_HOURLY_RATE = 60.00;
    public static final int SLOTS_REQUIRED = 2;

    private double customRate = DEFAULT_HOURLY_RATE;

    public Truck(String vehicleNumber, String ownerName, String contactNumber) {
        super(vehicleNumber, ownerName, VehicleType.TRUCK, contactNumber);
    }

    public Truck(String vehicleNumber, String ownerName, String contactNumber, double customRate) {
        super(vehicleNumber, ownerName, VehicleType.TRUCK, contactNumber);
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
        return "Truck [Reg: " + getVehicleNumber() + ", Owner: " + getOwnerName() + 
               ", Rate: ₹" + getHourlyRate() + "/hr, Slots: " + getRequiredSlots() + " consecutive]";
    }
}

