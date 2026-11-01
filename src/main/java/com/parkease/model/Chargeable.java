package com.parkease.model;

import java.time.LocalDateTime;

/**
 * Interface demonstrating Abstraction and Polymorphism in Java.
 * Any vehicle or entity that incurs parking charges implements Chargeable.
 */
public interface Chargeable {

    // Maximum daily chargeable hours cap
    int MAX_BILLING_HOURS = 8;

    /**
     * Calculates fee based on total whole/ceiling hours.
     * Demonstrates Method Overloading.
     * 
     * @param hours total hours parked
     * @return final calculated fee adhering to the 8-hour cap rule
     */
    double calculateCharge(int hours);

    /**
     * Overloaded method: Calculates fee based on duration in minutes.
     * Partial hours are rounded UP to the next complete hour.
     * 
     * @param minutes duration in minutes
     * @return final calculated charge
     */
    double calculateCharge(long minutes);

    /**
     * Overloaded method: Calculates fee based on entry and exit timestamps.
     * 
     * @param entryTime entry timestamp
     * @param exitTime exit timestamp
     * @return final calculated charge
     */
    double calculateCharge(LocalDateTime entryTime, LocalDateTime exitTime);
}

