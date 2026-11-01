# ParkEase – Smart Parking Management System

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)](https://getbootstrap.com/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)](https://www.thymeleaf.org/)

> **Tagline:** *Smart Parking. Easy Management.*  
> **Course / Subject:** Object-Oriented Programming (Java) & Database Management Systems (DBMS) Academic Capstone Project.

---

## 1. Project Overview
**ParkEase** is a modern, responsive, full-stack Smart Parking Management Platform designed to streamline commercial and institutional parking operations. It manages fixed 20-bay parking facilities, handles vehicle check-in/check-out, calculates hourly tariffs with daily caps, produces printable receipts/tickets, and maintains complete transactional audits in a relational MySQL database.

---

## 2. Technology Stack & Architecture

### Architectural Flow:
```
[ Responsive Frontend (HTML5 / Bootstrap 5 / JS / Chart.js / Thymeleaf) ]
                               │
                               ▼
        [ Spring Boot Web Controllers (@Controller / @RestController) ]
                               │
                               ▼
       [ Service Layer (ParkingService / BillingService / ReportService) ]
          ├─► [ Java OOP Domain Models (Bike, Car, Truck, Chargeable) ]
                               │
                               ▼
     [ Repository Layer (Spring Data JPA / Hibernate ORM / Transactions) ]
                               │
                               ▼
              [ MySQL 8 Relational Database (parkease_db) ]
```

* **Backend:** Java 17+, Spring Boot 3, Spring Data JPA, Hibernate ORM, JDBC
* **Frontend:** Thymeleaf 3, Bootstrap 5.3, JavaScript (ES6+), Chart.js 4, FontAwesome 6
* **Database:** MySQL 8 (with intelligent automatic fallback for offline lab demos)
* **Build System:** Apache Maven 3.6+

---

## 3. Java OOP Concepts Demonstrated

The core business logic strictly adheres to Object-Oriented Programming principles:

| OOP Concept | Project Implementation |
| :--- | :--- |
| **Abstraction** | `Vehicle` abstract parent class defines abstract methods `getHourlyRate()` and `getRequiredSlots()`. Abstract interface `Chargeable` contracts tariff calculation. |
| **Inheritance** | Child classes `Bike`, `Car`, and `Truck` extend parent `Vehicle`, inheriting common fields (`vehicleNumber`, `ownerName`, `contactNumber`). |
| **Polymorphism** | `Chargeable` interface defines `calculateCharge()`. `Bike` (₹10/hr), `Car` (₹30/hr), and `Truck` (₹60/hr) provide overridden implementations. |
| **Method Overloading** | Overloaded `calculateCharge(int hours)`, `calculateCharge(long minutes)`, and `calculateCharge(LocalDateTime entry, LocalDateTime exit)`. |
| **Encapsulation** | Private fields with validated getters and setters across entities, DTOs, and domain models. |
| **Enums** | `VehicleType` (BIKE, CAR, TRUCK), `SlotStatus` (AVAILABLE, OCCUPIED, RESERVED), `TicketStatus` (ACTIVE, COMPLETED), `PaymentMethod` (CASH, UPI, CARD). |
| **Static & Final** | Constants such as `MAX_BILLING_HOURS = 8`, `TOTAL_SLOTS = 20`, and `CURRENCY_SYMBOL = "₹"`. |
| **Custom Exceptions** | `ParkingFullException`, `InvalidVehicleException`, `DuplicateVehicleException`, `TicketNotFoundException`, `InvalidTicketStateException`, `DuplicatePaymentException`. |
| **Collections API** | `List`, `Map`, `Set`, and Java Streams used for consecutive slot finding and statistical aggregation. |

---

## 4. DBMS Relational Schema (MySQL: `parkease_db`)

The relational database adheres to Boyce-Codd Normal Form (BCNF / 3NF) with primary keys, foreign keys, and indexes:

1. **`users`**: Administrator login credentials, role-based security, SHA-256 password hash.
2. **`vehicles`**: Registered vehicles (`vehicle_id` PK, `vehicle_number` UNIQUE, `vehicle_type`, `owner_name`, `contact_number`).
3. **`parking_slots`**: Exactly 20 bays (`slot_id` PK, `slot_number` UNIQUE, `status`, `current_vehicle_id` FK).
4. **`parking_tickets`**: Parking tickets (`ticket_id` PK, `ticket_number` UNIQUE, `vehicle_id` FK, `entry_time`, `exit_time`, `allocated_slots`, `total_amount`, `ticket_status`).
5. **`payments`**: Payment records (`payment_id` PK, `ticket_id` FK, `amount`, `payment_method`, `payment_status`, `payment_time`).
6. **`vehicle_rates`**: Configurable tariffs (`rate_id` PK, `vehicle_type` UNIQUE, `hourly_rate`, `slots_required`).

---

## 5. Main Functional Modules & Pages

1. **Dashboard (`/dashboard`)**: 20 Total Slots, Occupied, Available, Vehicles Parked Today, Today's Revenue, Active Tickets, Chart.js Occupancy Donut, 7-day Revenue Bar Chart, Recent Activity Stream.
2. **Park Vehicle (`/park-vehicle`)**: Registration form, duplicate active vehicle rejection, consecutive slot allocator for trucks, printable ticket modal.
3. **Vehicle Exit (`/vehicle-exit`)**: Search by Ticket Number or Vehicle Number, mathematical duration calculation with 8-hour daily cap, simulated payment, printable receipt modal.
4. **Live Parking Slots (`/parking-slots`)**: Visual 20-bay grid (Green = Available, Red = Occupied, Gray = Reserved), filterable by state.
5. **Parking Tickets (`/tickets`)**: Master ticket log with search, vehicle category filters, status filters, and ticket inspection modals.
6. **Vehicle Directory (`/vehicles`)**: Master database of all registered vehicle profiles and historical visits.
7. **Billing & Payments (`/billing`)**: Financial metrics (Total, Today, Weekly, Monthly), vehicle category breakdown, full payment transaction ledger.
8. **Reports & Analytics (`/reports`)**: Date-range filtered audit logs, KPI cards, and one-click **Export to CSV**.
9. **Database Records (`/database-records`)**: Dedicated DBMS inspector displaying all 6 MySQL tables, raw schemas, row counts, and sample academic SQL Joins / Aggregates.
10. **Settings (`/settings`)**: Dynamic hourly rates management and system specifications.
11. **Login & Logout (`/login`, `/logout`)**: Admin authentication with session management.

---

## 6. How to Run the Application

### Prerequisites:
- Java JDK 17 or later (OpenJDK / Oracle JDK)
- Maven 3.6+ (or use the provided `mvnw.cmd`)
- MySQL 8.0 (Optional; built-in automatic fallback allows instant demo even if MySQL is offline!)

### Step 1: Database Setup (MySQL)
Open **MySQL Workbench** or command-line client and execute:
```sql
CREATE DATABASE parkease_db;
```
*(Optional)* You can import the full standalone script located in [`database/parkease_db.sql`](database/parkease_db.sql).

Verify credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/parkease_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
```

### Step 2: Build & Run
From the project root directory, run:
```bash
# Using standard Maven:
mvn spring-boot:run

# Or using the Windows wrapper:
.\mvnw.cmd spring-boot:run
```

### Step 3: Access the Application
Open your web browser and navigate to:
```
http://localhost:8080
```

### Default Admin Credentials:
* **Username:** `admin`
* **Password:** `admin123`

---

## 7. Tariff & Billing Formula
* **Bike:** ₹10 / hour (Requires 1 slot)
* **Car:** ₹30 / hour (Requires 1 slot)
* **Truck:** ₹60 / hour (Requires 2 consecutive slots)
* **Partial Hours:** Rounded up using `CEILING(Duration in Hours)`
* **Daily Cap:** Maximum **8 chargeable hours** per session:
  $$\text{Charge} = \min(\lceil\text{Hours}\rceil, 8) \times \text{Hourly Rate}$$

---

## 8. Verification & Test Suite
Run the automated test suite covering OOP and database scenarios:
```bash
mvn test
```
* **Scenario 1:** Park bike (allocates 1 slot)
* **Scenario 2:** Park car (allocates 1 slot)
* **Scenario 3:** Park truck (allocates 2 consecutive slots: e.g. Bay 1 & Bay 2)
* **Scenario 4:** Reject duplicate active vehicle registration
* **Scenario 5:** Calculate charges with 8-hour cap rule verified
* **Scenario 6:** Check out vehicle, verify payment recording, release slots back to AVAILABLE
* **Scenario 7:** Prevent duplicate payments on completed tickets

