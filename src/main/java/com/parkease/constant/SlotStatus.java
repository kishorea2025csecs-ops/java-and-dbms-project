package com.parkease.constant;

public enum SlotStatus {
    AVAILABLE("Available", "bg-success text-white"),
    OCCUPIED("Occupied", "bg-danger text-white"),
    RESERVED("Reserved", "bg-secondary text-white");

    private final String displayName;
    private final String badgeClass;

    SlotStatus(String displayName, String badgeClass) {
        this.displayName = displayName;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeClass() {
        return badgeClass;
    }
}

