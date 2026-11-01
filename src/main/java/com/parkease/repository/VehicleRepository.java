package com.parkease.repository;

import com.parkease.entity.VehicleEntity;
import com.parkease.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {

    Optional<VehicleEntity> findByVehicleNumberIgnoreCase(String vehicleNumber);

    List<VehicleEntity> findByVehicleType(VehicleType vehicleType);

    @Query("SELECT v FROM VehicleEntity v ORDER BY v.createdAt DESC")
    List<VehicleEntity> findAllOrderByCreatedAtDesc();

    long countByVehicleType(VehicleType vehicleType);
}

