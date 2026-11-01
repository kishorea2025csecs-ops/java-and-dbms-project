-- ==============================================================================
-- PARKEASE - SMART PARKING MANAGEMENT SYSTEM
-- Standalone MySQL 8 Database Creation and DBMS Academic Script
-- ==============================================================================

DROP DATABASE IF EXISTS parkease_db;
CREATE DATABASE parkease_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE parkease_db;

-- ------------------------------------------------------------------------------
-- TABLE 1: users (Administrator Access)
-- ------------------------------------------------------------------------------
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_ADMIN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ------------------------------------------------------------------------------
-- TABLE 2: vehicles (Registered Vehicles)
-- ------------------------------------------------------------------------------
CREATE TABLE vehicles (
    vehicle_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_name VARCHAR(100) NOT NULL,
    vehicle_number VARCHAR(20) NOT NULL UNIQUE,
    vehicle_type ENUM('BIKE', 'CAR', 'TRUCK') NOT NULL,
    contact_number VARCHAR(15) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ------------------------------------------------------------------------------
-- TABLE 3: parking_slots (20 Parking Bays)
-- ------------------------------------------------------------------------------
CREATE TABLE parking_slots (
    slot_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    slot_number INT NOT NULL UNIQUE,
    status ENUM('AVAILABLE', 'OCCUPIED', 'RESERVED') NOT NULL DEFAULT 'AVAILABLE',
    current_vehicle_id BIGINT NULL,
    CONSTRAINT fk_slot_vehicle FOREIGN KEY (current_vehicle_id) 
        REFERENCES vehicles(vehicle_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------------------------
-- TABLE 4: parking_tickets (Active and Historic Tickets)
-- ------------------------------------------------------------------------------
CREATE TABLE parking_tickets (
    ticket_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_number VARCHAR(50) NOT NULL UNIQUE,
    vehicle_id BIGINT NOT NULL,
    entry_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    exit_time TIMESTAMP NULL,
    parking_hours INT NULL,
    allocated_slots VARCHAR(50) NOT NULL,
    total_amount DECIMAL(10, 2) DEFAULT 0.00,
    ticket_status ENUM('ACTIVE', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT fk_ticket_vehicle FOREIGN KEY (vehicle_id) 
        REFERENCES vehicles(vehicle_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------------------------
-- TABLE 5: payments (Simulated Payment Transactions)
-- ------------------------------------------------------------------------------
CREATE TABLE payments (
    payment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_method ENUM('CASH', 'UPI', 'CARD') NOT NULL,
    payment_status ENUM('PAID', 'PENDING', 'FAILED') NOT NULL DEFAULT 'PAID',
    payment_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payment_ticket FOREIGN KEY (ticket_id) 
        REFERENCES parking_tickets(ticket_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------------------------
-- TABLE 6: vehicle_rates (Hourly Pricing & Slot Allocation)
-- ------------------------------------------------------------------------------
CREATE TABLE vehicle_rates (
    rate_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_type ENUM('BIKE', 'CAR', 'TRUCK') NOT NULL UNIQUE,
    hourly_rate DECIMAL(10, 2) NOT NULL,
    slots_required INT NOT NULL
) ENGINE=InnoDB;

-- ------------------------------------------------------------------------------
-- INDEXES FOR QUERY OPTIMIZATION
-- ------------------------------------------------------------------------------
CREATE INDEX idx_veh_number ON vehicles(vehicle_number);
CREATE INDEX idx_ticket_num ON parking_tickets(ticket_number);
CREATE INDEX idx_ticket_status ON parking_tickets(ticket_status);
CREATE INDEX idx_slot_status ON parking_slots(status);

-- ------------------------------------------------------------------------------
-- SEED DATA INSERTION
-- ------------------------------------------------------------------------------

-- Default Admin (Password: admin123 hashed)
INSERT INTO users (username, password_hash, role) 
VALUES ('admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'ROLE_ADMIN');

-- Standard Rates
INSERT INTO vehicle_rates (vehicle_type, hourly_rate, slots_required) VALUES
('BIKE', 10.00, 1),
('CAR', 30.00, 1),
('TRUCK', 60.00, 2);

-- Populate 20 Standard Slots
INSERT INTO parking_slots (slot_number, status) VALUES
(1, 'AVAILABLE'), (2, 'AVAILABLE'), (3, 'AVAILABLE'), (4, 'AVAILABLE'), (5, 'AVAILABLE'),
(6, 'AVAILABLE'), (7, 'AVAILABLE'), (8, 'AVAILABLE'), (9, 'AVAILABLE'), (10, 'AVAILABLE'),
(11, 'AVAILABLE'), (12, 'AVAILABLE'), (13, 'AVAILABLE'), (14, 'AVAILABLE'), (15, 'AVAILABLE'),
(16, 'AVAILABLE'), (17, 'AVAILABLE'), (18, 'AVAILABLE'), (19, 'AVAILABLE'), (20, 'AVAILABLE');

-- Sample Vehicle Records
INSERT INTO vehicles (vehicle_id, owner_name, vehicle_number, vehicle_type, contact_number, created_at) VALUES
(1, 'Rahul Sharma', 'MH-02-AB-1234', 'CAR', '9876543210', NOW() - INTERVAL 5 DAY),
(2, 'Priya Patel', 'MH-12-CD-5678', 'BIKE', '9823456789', NOW() - INTERVAL 4 DAY),
(3, 'Amit Verma', 'DL-01-XY-9999', 'TRUCK', '9811223344', NOW() - INTERVAL 3 DAY),
(4, 'Sneha Joshi', 'KA-03-EF-4321', 'CAR', '9845012345', NOW() - INTERVAL 2 DAY),
(5, 'Vikram Singh', 'MH-14-GH-8888', 'CAR', '9765432100', NOW() - INTERVAL 2 HOUR),
(6, 'Ananya Roy', 'WB-02-JK-3456', 'BIKE', '9830123456', NOW() - INTERVAL 1 HOUR);

-- Sample Completed Historic Tickets
INSERT INTO parking_tickets (ticket_id, ticket_number, vehicle_id, entry_time, exit_time, parking_hours, allocated_slots, total_amount, ticket_status) VALUES
(1, 'TKT-20261101-0001', 1, NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 5 DAY + INTERVAL 3 HOUR, 3, '4', 90.00, 'COMPLETED'),
(2, 'TKT-20261101-0002', 2, NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 4 DAY + INTERVAL 2 HOUR, 2, '7', 20.00, 'COMPLETED'),
(3, 'TKT-20261101-0003', 3, NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 3 DAY + INTERVAL 4 HOUR, 4, '9, 10', 240.00, 'COMPLETED'),
(4, 'TKT-20261101-0004', 4, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY + INTERVAL 9 HOUR, 8, '2', 240.00, 'COMPLETED'); -- Cap applied: 8 hrs

-- Sample Payments
INSERT INTO payments (ticket_id, amount, payment_method, payment_status, payment_time) VALUES
(1, 90.00, 'UPI', 'PAID', NOW() - INTERVAL 5 DAY + INTERVAL 3 HOUR),
(2, 20.00, 'CASH', 'PAID', NOW() - INTERVAL 4 DAY + INTERVAL 2 HOUR),
(3, 240.00, 'CARD', 'PAID', NOW() - INTERVAL 3 DAY + INTERVAL 4 HOUR),
(4, 240.00, 'UPI', 'PAID', NOW() - INTERVAL 2 DAY + INTERVAL 9 HOUR);

-- Sample Active Parked Vehicles (Slot 3: Car, Slot 6: Bike)
INSERT INTO parking_tickets (ticket_id, ticket_number, vehicle_id, entry_time, exit_time, parking_hours, allocated_slots, total_amount, ticket_status) VALUES
(5, 'TKT-20261102-1005', 5, NOW() - INTERVAL 2 HOUR, NULL, NULL, '3', 0.00, 'ACTIVE'),
(6, 'TKT-20261102-1006', 6, NOW() - INTERVAL 1 HOUR, NULL, NULL, '6', 0.00, 'ACTIVE');

UPDATE parking_slots SET status = 'OCCUPIED', current_vehicle_id = 5 WHERE slot_number = 3;
UPDATE parking_slots SET status = 'OCCUPIED', current_vehicle_id = 6 WHERE slot_number = 6;

-- ------------------------------------------------------------------------------
-- ACADEMIC DBMS QUERIES DEMONSTRATION
-- ------------------------------------------------------------------------------
-- 1. Inner Join: Active Tickets with Vehicle & Owner Details
SELECT 
    t.ticket_number,
    v.vehicle_number,
    v.owner_name,
    v.vehicle_type,
    t.allocated_slots,
    t.entry_time
FROM parking_tickets t
JOIN vehicles v ON t.vehicle_id = v.vehicle_id
WHERE t.ticket_status = 'ACTIVE';

-- 2. Aggregate & GROUP BY: Total Revenue by Vehicle Category
SELECT 
    v.vehicle_type,
    COUNT(t.ticket_id) AS total_trips,
    COALESCE(SUM(p.amount), 0.00) AS total_revenue
FROM vehicles v
JOIN parking_tickets t ON v.vehicle_id = t.vehicle_id
JOIN payments p ON t.ticket_id = p.ticket_id
GROUP BY v.vehicle_type;

-- 3. Occupancy Rate Calculation
SELECT 
    COUNT(CASE WHEN status = 'OCCUPIED' THEN 1 END) AS occupied_slots,
    COUNT(CASE WHEN status = 'AVAILABLE' THEN 1 END) AS available_slots,
    ROUND((COUNT(CASE WHEN status = 'OCCUPIED' THEN 1 END) / COUNT(*)) * 100, 2) AS occupancy_percentage
FROM parking_slots;

