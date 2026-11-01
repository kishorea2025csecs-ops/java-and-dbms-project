package com.parkease.entity;

import com.parkease.model.VehicleType;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "vehicle_rates")
public class VehicleRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rate_id")
    private Long rateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false, unique = true, length = 20)
    private VehicleType vehicleType;

    @Column(name = "hourly_rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    @Column(name = "slots_required", nullable = false)
    private Integer slotsRequired;

    public VehicleRate() {}

    public VehicleRate(VehicleType vehicleType, BigDecimal hourlyRate, Integer slotsRequired) {
        this.vehicleType = vehicleType;
        this.hourlyRate = hourlyRate;
        this.slotsRequired = slotsRequired;
    }

    public Long getRateId() {
        return rateId;
    }

    public void setRateId(Long rateId) {
        this.rateId = rateId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Integer getSlotsRequired() {
        return slotsRequired;
    }

    public void setSlotsRequired(Integer slotsRequired) {
        this.slotsRequired = slotsRequired;
    }
}

