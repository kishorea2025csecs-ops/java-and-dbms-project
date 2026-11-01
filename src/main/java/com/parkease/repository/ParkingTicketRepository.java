package com.parkease.repository;

import com.parkease.constant.TicketStatus;
import com.parkease.entity.ParkingTicket;
import com.parkease.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingTicketRepository extends JpaRepository<ParkingTicket, Long> {

    Optional<ParkingTicket> findByTicketNumberIgnoreCase(String ticketNumber);

    @Query("SELECT t FROM ParkingTicket t WHERE UPPER(t.vehicle.vehicleNumber) = UPPER(:vehicleNumber) AND t.ticketStatus = 'ACTIVE'")
    Optional<ParkingTicket> findActiveTicketByVehicleNumber(@Param("vehicleNumber") String vehicleNumber);

    List<ParkingTicket> findByTicketStatusOrderByEntryTimeDesc(TicketStatus ticketStatus);

    @Query("SELECT t FROM ParkingTicket t ORDER BY t.entryTime DESC")
    List<ParkingTicket> findAllOrderByEntryTimeDesc();

    long countByTicketStatus(TicketStatus status);

    @Query("SELECT COUNT(t) FROM ParkingTicket t WHERE t.entryTime >= :startTime AND t.entryTime <= :endTime")
    long countTicketsBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT t FROM ParkingTicket t WHERE " +
           "(:keyword IS NULL OR LOWER(t.ticketNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(t.vehicle.vehicleNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(t.vehicle.ownerName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:status IS NULL OR t.ticketStatus = :status) " +
           "AND (:vehicleType IS NULL OR t.vehicle.vehicleType = :vehicleType) " +
           "ORDER BY t.entryTime DESC")
    List<ParkingTicket> searchTickets(
            @Param("keyword") String keyword,
            @Param("status") TicketStatus status,
            @Param("vehicleType") VehicleType vehicleType);

    @Query("SELECT t FROM ParkingTicket t WHERE t.entryTime >= :start AND t.entryTime <= :end ORDER BY t.entryTime DESC")
    List<ParkingTicket> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}

