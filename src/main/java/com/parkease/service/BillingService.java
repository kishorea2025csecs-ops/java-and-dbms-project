package com.parkease.service;

import com.parkease.constant.PaymentStatus;
import com.parkease.entity.Payment;
import com.parkease.model.VehicleType;
import com.parkease.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BillingService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public BillingService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public BigDecimal getTotalRevenue() {
        return paymentRepository.calculateTotalRevenue();
    }

    public BigDecimal getTodayRevenue() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        return paymentRepository.calculateRevenueBetween(startOfDay, endOfDay);
    }

    public BigDecimal getWeeklyRevenue() {
        LocalDateTime startOfWeek = LocalDate.now().minusDays(7).atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        return paymentRepository.calculateRevenueBetween(startOfWeek, now);
    }

    public BigDecimal getMonthlyRevenue() {
        LocalDateTime startOfMonth = LocalDate.now().minusDays(30).atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        return paymentRepository.calculateRevenueBetween(startOfMonth, now);
    }

    public BigDecimal getRevenueByVehicleType(VehicleType type) {
        return paymentRepository.calculateRevenueByVehicleType(type);
    }

    public Map<String, BigDecimal> getRevenueBreakdown() {
        Map<String, BigDecimal> map = new HashMap<>();
        for (VehicleType type : VehicleType.values()) {
            map.put(type.name(), getRevenueByVehicleType(type));
        }
        return map;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAllOrderByPaymentTimeDesc();
    }

    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByPaymentStatusOrderByPaymentTimeDesc(status);
    }
}

