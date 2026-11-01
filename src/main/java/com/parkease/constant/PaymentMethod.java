package com.parkease.constant;

public enum PaymentMethod {
    CASH("Cash Payment", "fa-money-bill-wave"),
    UPI("UPI / QR Code", "fa-qrcode"),
    CARD("Credit/Debit Card", "fa-credit-card");

    private final String description;
    private final String iconClass;

    PaymentMethod(String description, String iconClass) {
        this.description = description;
        this.iconClass = iconClass;
    }

    public String getDescription() {
        return description;
    }

    public String getIconClass() {
        return iconClass;
    }
}

