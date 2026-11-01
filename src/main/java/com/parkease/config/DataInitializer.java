package com.parkease.config;

import com.parkease.constant.PaymentMethod;
import com.parkease.constant.PaymentStatus;
import com.parkease.constant.SlotStatus;
import com.parkease.constant.TicketStatus;
import com.parkease.entity.*;
import com.parkease.model.VehicleType;
import com.parkease.repository.*;
import com.parkease.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final VehicleRateRepository rateRepository;
    private final ParkingSlotRepository slotRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingTicketRepository ticketRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public DataInitializer(
            UserRepository userRepository,
            VehicleRateRepository rateRepository,
            ParkingSlotRepository slotRepository,
            VehicleRepository vehicleRepository,
            ParkingTicketRepository ticketRepository,
            PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.rateRepository = rateRepository;
        this.slotRepository = slotRepository;
        this.vehicleRepository = vehicleRepository;
        this.ticketRepository = ticketRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // 1. Initialize / Update Admin User (Password: admin123)
        Optional<User> existingAdmin = userRepository.findByUsername("admin");
        if (existingAdmin.isPresent()) {
            User admin = existingAdmin.get();
            admin.setPasswordHash(UserService.hashPassword("admin123"));
            userRepository.save(admin);
        } else {
            User admin = new User("admin", UserService.hashPassword("admin123"), "ROLE_ADMIN");
            userRepository.save(admin);
        }

        // 2. Initialize Vehicle Rates
        if (rateRepository.count() == 0) {
            rateRepository.save(new VehicleRate(VehicleType.BIKE, BigDecimal.valueOf(10.00), 1));
            rateRepository.save(new VehicleRate(VehicleType.CAR, BigDecimal.valueOf(30.00), 1));
            rateRepository.save(new VehicleRate(VehicleType.TRUCK, BigDecimal.valueOf(60.00), 2));
        }

        // 3. Initialize Exactly 20 Parking Slots if fewer than 20
        if (slotRepository.count() < 20) {
            for (int i = 1; i <= 20; i++) {
                String slotName = String.format("SLOT-%02d", i);
                if (slotRepository.findBySlotNumberIgnoreCase(slotName).isEmpty()) {
                    slotRepository.save(new ParkingSlot(slotName));
                }
            }
        }

        // 4. Populate realistic demo records if tickets table is empty
        if (ticketRepository.count() == 0) {
            seedDemoData();
        }
    }

    private void seedDemoData() {
        LocalDateTime now = LocalDateTime.now();

        // Sample Historical Vehicles
        VehicleEntity car1 = vehicleRepository.save(new VehicleEntity("Rahul Sharma", "MH-02-AB-1234", VehicleType.CAR, "9876543210"));
        VehicleEntity bike1 = vehicleRepository.save(new VehicleEntity("Priya Patel", "MH-12-CD-5678", VehicleType.BIKE, "9823456789"));
        VehicleEntity truck1 = vehicleRepository.save(new VehicleEntity("Amit Verma", "DL-01-XY-9999", VehicleType.TRUCK, "9811223344"));
        VehicleEntity car2 = vehicleRepository.save(new VehicleEntity("Sneha Joshi", "KA-03-EF-4321", VehicleType.CAR, "9845012345"));

        // Historical Completed Tickets
        // Trip 1: Rahul (Car, 3 hrs = ₹90)
        ParkingTicket t1 = new ParkingTicket("TKT-20261101-0001", car1, now.minusDays(3).minusHours(4), "4");
        t1.setExitTime(now.minusDays(3).minusHours(1));
        t1.setParkingHours(3);
        t1.setTotalAmount(BigDecimal.valueOf(90.00));
        t1.setTicketStatus(TicketStatus.COMPLETED);
        ticketRepository.save(t1);
        paymentRepository.save(new Payment(t1, BigDecimal.valueOf(90.00), PaymentMethod.UPI, PaymentStatus.PAID));

        // Trip 2: Priya (Bike, 2 hrs = ₹20)
        ParkingTicket t2 = new ParkingTicket("TKT-20261101-0002", bike1, now.minusDays(2).minusHours(3), "7");
        t2.setExitTime(now.minusDays(2).minusHours(1));
        t2.setParkingHours(2);
        t2.setTotalAmount(BigDecimal.valueOf(20.00));
        t2.setTicketStatus(TicketStatus.COMPLETED);
        ticketRepository.save(t2);
        paymentRepository.save(new Payment(t2, BigDecimal.valueOf(20.00), PaymentMethod.CASH, PaymentStatus.PAID));

        // Trip 3: Amit (Truck, 4 hrs = ₹240)
        ParkingTicket t3 = new ParkingTicket("TKT-20261101-0003", truck1, now.minusDays(1).minusHours(5), "9, 10");
        t3.setExitTime(now.minusDays(1).minusHours(1));
        t3.setParkingHours(4);
        t3.setTotalAmount(BigDecimal.valueOf(240.00));
        t3.setTicketStatus(TicketStatus.COMPLETED);
        ticketRepository.save(t3);
        paymentRepository.save(new Payment(t3, BigDecimal.valueOf(240.00), PaymentMethod.CARD, PaymentStatus.PAID));

        // Trip 4: Sneha (Car, 10 hrs parked -> capped at 8 hrs = ₹240)
        ParkingTicket t4 = new ParkingTicket("TKT-20261101-0004", car2, now.minusHours(10), "2");
        t4.setExitTime(now.minusHours(2));
        t4.setParkingHours(8);
        t4.setTotalAmount(BigDecimal.valueOf(240.00));
        t4.setTicketStatus(TicketStatus.COMPLETED);
        ticketRepository.save(t4);
        paymentRepository.save(new Payment(t4, BigDecimal.valueOf(240.00), PaymentMethod.UPI, PaymentStatus.PAID));

        // Active Parked Vehicles in the lot right now!
        // Active 1: Vikram Singh (Car) parked at Slot 3
        VehicleEntity car3 = vehicleRepository.save(new VehicleEntity("Vikram Singh", "MH-14-GH-8888", VehicleType.CAR, "9765432100"));
        ParkingTicket active1 = new ParkingTicket("TKT-20261102-1005", car3, now.minusMinutes(135), "SLOT-03");
        ticketRepository.save(active1);
        slotRepository.findBySlotNumberIgnoreCase("SLOT-03").ifPresent(s -> {
            s.setStatus(SlotStatus.OCCUPIED);
            s.setCurrentVehicle(car3);
            slotRepository.save(s);
        });

        // Active 2: Ananya Roy (Bike) parked at Slot 6
        VehicleEntity bike2 = vehicleRepository.save(new VehicleEntity("Ananya Roy", "WB-02-JK-3456", VehicleType.BIKE, "9830123456"));
        ParkingTicket active2 = new ParkingTicket("TKT-20261102-1006", bike2, now.minusMinutes(45), "SLOT-06");
        ticketRepository.save(active2);
        slotRepository.findBySlotNumberIgnoreCase("SLOT-06").ifPresent(s -> {
            s.setStatus(SlotStatus.OCCUPIED);
            s.setCurrentVehicle(bike2);
            slotRepository.save(s);
        });

        // Active 3: Raj Malhotra (Truck) parked at Slots 11 & 12
        VehicleEntity truck2 = vehicleRepository.save(new VehicleEntity("Raj Malhotra", "HR-26-TR-7777", VehicleType.TRUCK, "9910123456"));
        ParkingTicket active3 = new ParkingTicket("TKT-20261102-1007", truck2, now.minusMinutes(90), "SLOT-11, SLOT-12");
        ticketRepository.save(active3);
        slotRepository.findBySlotNumberIgnoreCase("SLOT-11").ifPresent(s -> {
            s.setStatus(SlotStatus.OCCUPIED);
            s.setCurrentVehicle(truck2);
            slotRepository.save(s);
        });
        slotRepository.findBySlotNumberIgnoreCase("SLOT-12").ifPresent(s -> {
            s.setStatus(SlotStatus.OCCUPIED);
            s.setCurrentVehicle(truck2);
            slotRepository.save(s);
        });

        // Slot 18 reserved for maintenance/VIP
        slotRepository.findBySlotNumberIgnoreCase("SLOT-18").ifPresent(s -> {
            s.setStatus(SlotStatus.RESERVED);
            slotRepository.save(s);
        });
    }
}

