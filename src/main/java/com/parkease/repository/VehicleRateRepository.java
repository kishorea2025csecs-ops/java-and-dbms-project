package com.parkease.repository;

import com.parkease.entity.VehicleRate;
import com.parkease.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VehicleRateRepository extends JpaRepository<VehicleRate, Long> {
    Optional<VehicleRate> findByVehicleType(VehicleType vehicleType);
}

