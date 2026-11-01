package com.parkease.service;

import com.parkease.entity.VehicleEntity;
import com.parkease.exception.InvalidVehicleException;
import com.parkease.model.*;
import com.parkease.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Service managing Vehicles and OOP Model Instantiation.
 * Demonstrates the bridge between Relational Database Entities and OOP Models.
 */
@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Finds or creates a Vehicle entity in the database.
     */
    @Transactional
    public VehicleEntity findOrCreateVehicle(String ownerName, String vehicleNumber, VehicleType vehicleType, String contactNumber) {
        if (vehicleNumber == null || vehicleNumber.trim().isEmpty()) {
            throw new InvalidVehicleException("Vehicle registration number is required.");
        }
        String cleanNumber = vehicleNumber.toUpperCase().trim();

        Optional<VehicleEntity> existing = vehicleRepository.findByVehicleNumberIgnoreCase(cleanNumber);
        if (existing.isPresent()) {
            VehicleEntity vehicle = existing.get();
            // Update contact or owner name if provided
            if (ownerName != null && !ownerName.trim().isEmpty()) {
                vehicle.setOwnerName(ownerName.trim());
            }
            if (contactNumber != null && !contactNumber.trim().isEmpty()) {
                vehicle.setContactNumber(contactNumber.trim());
            }
            vehicle.setVehicleType(vehicleType);
            return vehicleRepository.save(vehicle);
        }

        VehicleEntity newVehicle = new VehicleEntity(ownerName, cleanNumber, vehicleType, contactNumber);
        return vehicleRepository.save(newVehicle);
    }

    /**
     * Factory method demonstrating OOP Polymorphism:
     * Instantiates specialized child classes (Bike, Car, Truck) based on VehicleType.
     */
    public Vehicle createOopVehicle(VehicleEntity entity, double customHourlyRate) {
        if (entity == null) {
            return null;
        }
        VehicleType type = entity.getVehicleType();
        switch (type) {
            case BIKE:
                return new Bike(entity.getVehicleNumber(), entity.getOwnerName(), entity.getContactNumber(), customHourlyRate);
            case CAR:
                return new Car(entity.getVehicleNumber(), entity.getOwnerName(), entity.getContactNumber(), customHourlyRate);
            case TRUCK:
                return new Truck(entity.getVehicleNumber(), entity.getOwnerName(), entity.getContactNumber(), customHourlyRate);
            default:
                throw new InvalidVehicleException("Unsupported vehicle type: " + type);
        }
    }

    public List<VehicleEntity> getAllVehicles() {
        return vehicleRepository.findAllOrderByCreatedAtDesc();
    }

    public Optional<VehicleEntity> getVehicleByNumber(String vehicleNumber) {
        return vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber);
    }
}

