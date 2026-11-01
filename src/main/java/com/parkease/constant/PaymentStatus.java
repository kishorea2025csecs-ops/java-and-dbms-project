package com.parkease.constant;

public enum PaymentStatus {
    PAID("Paid", "badge-paid"),
    PENDING("Pending", "badge-pending"),
    FAILED("Failed", "badge-failed");

    private final String displayName;
    private final String cssClass;

    PaymentStatus(String displayName, String cssClass) {
        this.displayName = displayName;
        this.cssClass = cssClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCssClass() {
        return cssClass;
    }
}

