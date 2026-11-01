package com.parkease;

import com.parkease.model.Bike;
import com.parkease.model.Car;
import com.parkease.model.ParkingLot;
import com.parkease.model.Truck;
import com.parkease.model.Vehicle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit & Business Logic Test Suite for ParkEase.
 * Demonstrates Java OOP Principles, Polymorphism, and Tariff Rules.
 */
class ParkEaseApplicationTests {

    @Test
    @DisplayName("OOP Concept Test: Verify Hourly Rates, Polymorphism, and 8-Hour Billing Cap")
    void testOopChargeCalculationAndCap() {
        Bike bike = new Bike("DL-01-BK-0001", "Test Bike", "9999999991");
        Car car = new Car("DL-01-CR-0002", "Test Car", "9999999992");
        Truck truck = new Truck("DL-01-TR-0003", "Test Truck", "9999999993");

        // 1. Hourly Rates Check
        assertEquals(10.00, bike.getHourlyRate());
        assertEquals(30.00, car.getHourlyRate());
        assertEquals(60.00, truck.getHourlyRate());

        // 2. Normal Duration (3 hours)
        assertEquals(30.00, bike.calculateCharge(3));  // 3 * 10
        assertEquals(90.00, car.calculateCharge(3));   // 3 * 30
        assertEquals(180.00, truck.calculateCharge(3)); // 3 * 60

        // 3. Billing Cap Rule: Maximum 8 chargeable hours
        // 12 hours parked: Charge = min(12, 8) * rate
        assertEquals(80.00, bike.calculateCharge(12));   // 8 * 10 = 80
        assertEquals(240.00, car.calculateCharge(12));   // 8 * 30 = 240
        assertEquals(480.00, truck.calculateCharge(12));  // 8 * 60 = 480

        // 4. Ceiling Rounding Rule: 75 minutes rounds up to 2 hours
        assertEquals(20.00, bike.calculateCharge(75L));  // 2 * 10 = 20
        assertEquals(60.00, car.calculateCharge(75L));   // 2 * 30 = 60
        assertEquals(120.00, truck.calculateCharge(75L)); // 2 * 60 = 120
    }

    @Test
    @DisplayName("OOP Consecutive Slot Test: Trucks require 2 consecutive slots")
    void testTruckConsecutiveSlotAllocation() {
        ParkingLot lot = new ParkingLot(20);

        Car car1 = new Car("MH-01-AA-1111", "User A", "9000000001");
        List<Integer> carSlots = lot.findAndAllocateSlots(car1);
        assertEquals(1, carSlots.size());
        assertEquals(1, carSlots.get(0)); // Slot 1

        // Next vehicle is a Truck (requires 2 consecutive slots: 2 & 3)
        Truck truck = new Truck("PB-10-TR-2222", "User B", "9000000002");
        List<Integer> truckSlots = lot.findAndAllocateSlots(truck);
        assertEquals(2, truckSlots.size());
        assertEquals(2, truckSlots.get(0));
        assertEquals(3, truckSlots.get(1));

        // Release slots
        lot.releaseSlots(truckSlots);
        assertTrue(lot.getSlots().get(1).isAvailable());
        assertTrue(lot.getSlots().get(2).isAvailable());
    }
}
