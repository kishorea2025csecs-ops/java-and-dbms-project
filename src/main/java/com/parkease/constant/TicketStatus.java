package com.parkease.constant;

public enum TicketStatus {
    ACTIVE("Active", "badge-active"),
    COMPLETED("Completed", "badge-completed"),
    CANCELLED("Cancelled", "badge-cancelled");

    private final String displayName;
    private final String cssClass;

    TicketStatus(String displayName, String cssClass) {
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

