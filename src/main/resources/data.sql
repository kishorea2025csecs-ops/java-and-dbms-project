-- ==============================================================================
-- ParkEase Seed Data Script
-- ==============================================================================

-- 1. Default Admin User (Password: admin123)
INSERT INTO users (user_id, username, password_hash, role, created_at)
VALUES (1, 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ROLE_ADMIN', CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE password_hash=VALUES(password_hash);

-- 2. Vehicle Rates (BIKE: ₹10, CAR: ₹30, TRUCK: ₹60)
INSERT INTO vehicle_rates (rate_id, vehicle_type, hourly_rate, slots_required)
VALUES 
    (1, 'BIKE', 10.00, 1),
    (2, 'CAR', 30.00, 1),
    (3, 'TRUCK', 60.00, 2)
ON DUPLICATE KEY UPDATE hourly_rate=VALUES(hourly_rate), slots_required=VALUES(slots_required);

-- 3. Initial 20 Parking Slots (All initially available)
INSERT INTO parking_slots (slot_id, slot_number, status, current_vehicle_id)
VALUES 
    (1, 1, 'AVAILABLE', NULL),
    (2, 2, 'AVAILABLE', NULL),
    (3, 3, 'AVAILABLE', NULL),
    (4, 4, 'AVAILABLE', NULL),
    (5, 5, 'AVAILABLE', NULL),
    (6, 6, 'AVAILABLE', NULL),
    (7, 7, 'AVAILABLE', NULL),
    (8, 8, 'AVAILABLE', NULL),
    (9, 9, 'AVAILABLE', NULL),
    (10, 10, 'AVAILABLE', NULL),
    (11, 11, 'AVAILABLE', NULL),
    (12, 12, 'AVAILABLE', NULL),
    (13, 13, 'AVAILABLE', NULL),
    (14, 14, 'AVAILABLE', NULL),
    (15, 15, 'AVAILABLE', NULL),
    (16, 16, 'AVAILABLE', NULL),
    (17, 17, 'AVAILABLE', NULL),
    (18, 18, 'AVAILABLE', NULL),
    (19, 19, 'AVAILABLE', NULL),
    (20, 20, 'AVAILABLE', NULL)
ON DUPLICATE KEY UPDATE slot_number=slot_number;

