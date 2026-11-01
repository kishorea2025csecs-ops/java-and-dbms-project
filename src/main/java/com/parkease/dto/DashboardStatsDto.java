package com.parkease.dto;

import java.math.BigDecimal;
import java.util.Map;

public class DashboardStatsDto {

    private int totalSlots = 20;
    private long occupiedSlots;
    private long availableSlots;
    private long reservedSlots;
    private long vehiclesParkedToday;
    private BigDecimal revenueCollectedToday;
    private long activeTickets;
    private double occupancyPercentage;

    private Map<String, Long> vehicleCategoryDistribution;
    private Map<String, BigDecimal> dailyRevenueLast7Days;

    public DashboardStatsDto() {
        this.revenueCollectedToday = BigDecimal.ZERO;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public long getOccupiedSlots() {
        return occupiedSlots;
    }

    public void setOccupiedSlots(long occupiedSlots) {
        this.occupiedSlots = occupiedSlots;
    }

    public long getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(long availableSlots) {
        this.availableSlots = availableSlots;
    }

    public long getReservedSlots() {
        return reservedSlots;
    }

    public void setReservedSlots(long reservedSlots) {
        this.reservedSlots = reservedSlots;
    }

    public long getVehiclesParkedToday() {
        return vehiclesParkedToday;
    }

    public void setVehiclesParkedToday(long vehiclesParkedToday) {
        this.vehiclesParkedToday = vehiclesParkedToday;
    }

    public BigDecimal getRevenueCollectedToday() {
        return revenueCollectedToday;
    }

    public void setRevenueCollectedToday(BigDecimal revenueCollectedToday) {
        this.revenueCollectedToday = revenueCollectedToday;
    }

    public long getActiveTickets() {
        return activeTickets;
    }

    public void setActiveTickets(long activeTickets) {
        this.activeTickets = activeTickets;
    }

    public double getOccupancyPercentage() {
        return occupancyPercentage;
    }

    public void setOccupancyPercentage(double occupancyPercentage) {
        this.occupancyPercentage = occupancyPercentage;
    }

    public Map<String, Long> getVehicleCategoryDistribution() {
        return vehicleCategoryDistribution;
    }

    public void setVehicleCategoryDistribution(Map<String, Long> vehicleCategoryDistribution) {
        this.vehicleCategoryDistribution = vehicleCategoryDistribution;
    }

    public Map<String, BigDecimal> getDailyRevenueLast7Days() {
        return dailyRevenueLast7Days;
    }

    public void setDailyRevenueLast7Days(Map<String, BigDecimal> dailyRevenueLast7Days) {
        this.dailyRevenueLast7Days = dailyRevenueLast7Days;
    }
}

