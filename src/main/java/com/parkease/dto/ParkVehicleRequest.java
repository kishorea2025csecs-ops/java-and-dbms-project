package com.parkease.dto;

import com.parkease.exception.InvalidVehicleException;
import com.parkease.model.VehicleType;

public class ParkVehicleRequest {

    private String ownerName;
    private String vehicleNumber;
    private VehicleType vehicleType;
    private String contactNumber;
    private String customEntryTime; // optional

    public ParkVehicleRequest() {}

    public ParkVehicleRequest(String ownerName, String vehicleNumber, VehicleType vehicleType, String contactNumber) {
        this.ownerName = ownerName;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.contactNumber = contactNumber;
    }

    public void validate() {
        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new InvalidVehicleException("Vehicle owner name is required.");
        }
        if (vehicleNumber == null || vehicleNumber.trim().isEmpty()) {
            throw new InvalidVehicleException("Vehicle registration number is required.");
        }
        if (vehicleType == null) {
            throw new InvalidVehicleException("Please select a valid vehicle category (BIKE, CAR, TRUCK).");
        }
        if (contactNumber == null || contactNumber.trim().isEmpty()) {
            throw new InvalidVehicleException("Owner contact number is required.");
        }
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
        this.vehicleNumber = vehicleNumber;
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

    public String getCustomEntryTime() {
        return customEntryTime;
    }

    public void setCustomEntryTime(String customEntryTime) {
        this.customEntryTime = customEntryTime;
    }
}
