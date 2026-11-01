package com.parkease.entity;

import com.parkease.model.VehicleType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles", indexes = {
    @Index(name = "idx_vehicles_num", columnList = "vehicle_number")
})
public class VehicleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Long vehicleId;

    @Column(name = "owner_name", nullable = false, length = 100)
    private String ownerName;

    @Column(name = "vehicle_number", nullable = false, unique = true, length = 20)
    private String vehicleNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false, length = 20)
    private VehicleType vehicleType;

    @Column(name = "contact_number", nullable = false, length = 15)
    private String contactNumber;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public VehicleEntity() {}

    public VehicleEntity(String ownerName, String vehicleNumber, VehicleType vehicleType, String contactNumber) {
        this.ownerName = ownerName;
        this.vehicleNumber = vehicleNumber.toUpperCase().trim();
        this.vehicleType = vehicleType;
        this.contactNumber = contactNumber;
        this.createdAt = LocalDateTime.now();
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber != null ? vehicleNumber.toUpperCase().trim() : null;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

