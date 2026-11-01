package com.parkease.model;

/**
 * Enum representing Vehicle Types with associated default hourly rate and slot requirement.
 * Demonstrates Java Enum concept with constructors, fields, and helper methods.
 */
public enum VehicleType {
    BIKE("Bike / Two-Wheeler", 10.00, 1, "fa-motorcycle"),
    CAR("Car / Four-Wheeler", 30.00, 1, "fa-car"),
    TRUCK("Truck / Heavy Vehicle", 60.00, 2, "fa-truck");

    private final String displayName;
    private final double defaultHourlyRate;
    private final int slotsRequired;
    private final String iconClass;

    VehicleType(String displayName, double defaultHourlyRate, int slotsRequired, String iconClass) {
        this.displayName = displayName;
        this.defaultHourlyRate = defaultHourlyRate;
        this.slotsRequired = slotsRequired;
        this.iconClass = iconClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getDefaultHourlyRate() {
        return defaultHourlyRate;
    }

    public int getSlotsRequired() {
        return slotsRequired;
    }

    public String getIconClass() {
        return iconClass;
    }
}

