package com.parkease.repository;

import com.parkease.constant.PaymentStatus;
import com.parkease.entity.Payment;
import com.parkease.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTicket_TicketId(Long ticketId);

    List<Payment> findByPaymentStatusOrderByPaymentTimeDesc(PaymentStatus status);

    @Query("SELECT p FROM Payment p ORDER BY p.paymentTime DESC")
    List<Payment> findAllOrderByPaymentTimeDesc();

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.paymentStatus = 'PAID'")
    BigDecimal calculateTotalRevenue();

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.paymentStatus = 'PAID' AND p.paymentTime >= :start AND p.paymentTime <= :end")
    BigDecimal calculateRevenueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.paymentStatus = 'PAID' AND p.ticket.vehicle.vehicleType = :vehicleType")
    BigDecimal calculateRevenueByVehicleType(@Param("vehicleType") VehicleType vehicleType);

    @Query("SELECT p.ticket.vehicle.vehicleType AS type, COUNT(p) AS count, COALESCE(SUM(p.amount), 0) AS totalRevenue " +
           "FROM Payment p WHERE p.paymentStatus = 'PAID' GROUP BY p.ticket.vehicle.vehicleType")
    List<Object[]> getRevenueDistributionRaw();
}

