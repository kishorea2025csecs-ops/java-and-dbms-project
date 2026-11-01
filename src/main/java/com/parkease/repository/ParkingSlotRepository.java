package com.parkease.repository;

import com.parkease.constant.SlotStatus;
import com.parkease.entity.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    List<ParkingSlot> findAllByOrderBySlotIdAsc();

    List<ParkingSlot> findAllByOrderBySlotNumberAsc();

    Optional<ParkingSlot> findBySlotNumberIgnoreCase(String slotNumber);

    List<ParkingSlot> findByStatusOrderBySlotIdAsc(SlotStatus status);

    long countByStatus(SlotStatus status);

    @Query("SELECT s FROM ParkingSlot s WHERE s.status = 'AVAILABLE' ORDER BY s.slotId ASC")
    List<ParkingSlot> findAvailableSlots();
}
