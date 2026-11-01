-- ==============================================================================
-- ParkEase Smart Parking Management System - Database Schema
-- DBMS Project - Relational Schema Definition
-- ==============================================================================

-- 1. Users Table (Administrator Authentication)
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_ADMIN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Vehicles Table
CREATE TABLE IF NOT EXISTS vehicles (
    vehicle_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_name VARCHAR(100) NOT NULL,
    vehicle_number VARCHAR(20) NOT NULL UNIQUE,
    vehicle_type VARCHAR(20) NOT NULL,
    contact_number VARCHAR(15) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Parking Slots Table (20 Slots)
CREATE TABLE IF NOT EXISTS parking_slots (
    slot_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    slot_number INT NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    current_vehicle_id BIGINT NULL,
    CONSTRAINT fk_slot_vehicle FOREIGN KEY (current_vehicle_id) REFERENCES vehicles(vehicle_id) ON DELETE SET NULL
);

-- 4. Parking Tickets Table
CREATE TABLE IF NOT EXISTS parking_tickets (
    ticket_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_number VARCHAR(50) NOT NULL UNIQUE,
    vehicle_id BIGINT NOT NULL,
    entry_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    exit_time TIMESTAMP NULL,
    parking_hours INT NULL,
    allocated_slots VARCHAR(50) NOT NULL,
    total_amount DECIMAL(10, 2) DEFAULT 0.00,
    ticket_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT fk_ticket_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id) ON DELETE CASCADE
);

-- 5. Payments Table
CREATE TABLE IF NOT EXISTS payments (
    payment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(30) NOT NULL,
    payment_status VARCHAR(20) NOT NULL DEFAULT 'PAID',
    payment_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payment_ticket FOREIGN KEY (ticket_id) REFERENCES parking_tickets(ticket_id) ON DELETE CASCADE
);

-- 6. Vehicle Rates Configuration Table
CREATE TABLE IF NOT EXISTS vehicle_rates (
    rate_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_type VARCHAR(20) NOT NULL UNIQUE,
    hourly_rate DECIMAL(10, 2) NOT NULL,
    slots_required INT NOT NULL
);

-- Performance Indexes
CREATE INDEX IF NOT EXISTS idx_vehicles_num ON vehicles(vehicle_number);
CREATE INDEX IF NOT EXISTS idx_tickets_num ON parking_tickets(ticket_number);
CREATE INDEX IF NOT EXISTS idx_tickets_status ON parking_tickets(ticket_status);
CREATE INDEX IF NOT EXISTS idx_slots_status ON parking_slots(status);

