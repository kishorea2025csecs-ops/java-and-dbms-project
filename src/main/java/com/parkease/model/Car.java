package com.parkease.model;

/**
 * Child Class Car extending Vehicle.
 * Demonstrates Inheritance, Polymorphism, and Method Overriding.
 */
public class Car extends Vehicle {

    public static final double DEFAULT_HOURLY_RATE = 30.00;
    public static final int SLOTS_REQUIRED = 1;

    private double customRate = DEFAULT_HOURLY_RATE;

    public Car(String vehicleNumber, String ownerName, String contactNumber) {
        super(vehicleNumber, ownerName, VehicleType.CAR, contactNumber);
    }

    public Car(String vehicleNumber, String ownerName, String contactNumber, double customRate) {
        super(vehicleNumber, ownerName, VehicleType.CAR, contactNumber);
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
        return "Car [Reg: " + getVehicleNumber() + ", Owner: " + getOwnerName() + 
               ", Rate: ₹" + getHourlyRate() + "/hr, Slots: " + getRequiredSlots() + "]";
    }
}

