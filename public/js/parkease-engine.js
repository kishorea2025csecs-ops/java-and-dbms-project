/**
 * ParkEase - Client-Side DBMS & OOP Parking Engine
 * Provides complete local persistence (localStorage) and business logic:
 * - 20 Fixed Slots allocation (Single bay for Bike/Car, 2 consecutive bays for Truck)
 * - Duration ceiling and 8-hour daily billing cap formula
 * - Full relational MySQL database emulation for Netlify deployment
 */

const ParkEaseDB = {
    STORAGE_KEY: 'parkease_cloud_db_v1',

    // Default Seed Data matching MySQL parkease_db
    getInitialData() {
        const slots = [];
        for (let i = 1; i <= 20; i++) {
            const numStr = i < 10 ? '0' + i : '' + i;
            slots.push({
                id: i,
                slotNumber: 'SLOT-' + numStr,
                displayNumber: 'SLOT-' + numStr,
                numericSlotNumber: i,
                status: i === 1 || i === 2 ? 'OCCUPIED' : 'AVAILABLE',
                vehicleNumber: i === 1 ? 'MH-12-AB-1234' : i === 2 ? 'MH-12-XY-9876' : null,
                ownerName: i === 1 ? 'Rajesh Kumar' : i === 2 ? 'Priya Sharma' : null,
                vehicleType: i === 1 ? 'CAR' : i === 2 ? 'BIKE' : null,
                ticketNumber: i === 1 ? 'TCK-1001' : i === 2 ? 'TCK-1002' : null
            });
        }

        const now = new Date();
        const twoHoursAgo = new Date(now.getTime() - 2.5 * 3600 * 1000).toISOString();
        const oneHourAgo = new Date(now.getTime() - 1.2 * 3600 * 1000).toISOString();
        const yesterday = new Date(now.getTime() - 24 * 3600 * 1000).toISOString();

        return {
            rates: {
                BIKE: { type: 'BIKE', name: 'Two Wheeler (Bike/Scooter)', rate: 10, bays: 1 },
                CAR: { type: 'CAR', name: 'Four Wheeler (Car/Sedan/SUV)', rate: 30, bays: 1 },
                TRUCK: { type: 'TRUCK', name: 'Heavy Vehicle (Truck/Bus)', rate: 60, bays: 2 }
            },
            slots: slots,
            tickets: [
                {
                    ticketNumber: 'TCK-1001',
                    vehicleNumber: 'MH-12-AB-1234',
                    ownerName: 'Rajesh Kumar',
                    vehicleType: 'CAR',
                    contactNumber: '9876543210',
                    allocatedSlots: 'SLOT-01',
                    slotIds: [1],
                    hourlyRate: 30,
                    entryTime: twoHoursAgo,
                    exitTime: null,
                    billedHours: 0,
                    totalAmount: 0,
                    status: 'ACTIVE'
                },
                {
                    ticketNumber: 'TCK-1002',
                    vehicleNumber: 'MH-12-XY-9876',
                    ownerName: 'Priya Sharma',
                    vehicleType: 'BIKE',
                    contactNumber: '9123456789',
                    allocatedSlots: 'SLOT-02',
                    slotIds: [2],
                    hourlyRate: 10,
                    entryTime: oneHourAgo,
                    exitTime: null,
                    billedHours: 0,
                    totalAmount: 0,
                    status: 'ACTIVE'
                },
                {
                    ticketNumber: 'TCK-1000',
                    vehicleNumber: 'DL-01-AA-5555',
                    ownerName: 'Amit Verma',
                    vehicleType: 'CAR',
                    contactNumber: '9988776655',
                    allocatedSlots: 'SLOT-05',
                    slotIds: [5],
                    hourlyRate: 30,
                    entryTime: yesterday,
                    exitTime: new Date(new Date(yesterday).getTime() + 3.2 * 3600 * 1000).toISOString(),
                    billedHours: 4,
                    totalAmount: 120,
                    status: 'COMPLETED'
                }
            ],
            payments: [
                {
                    receiptNumber: 'RCP-1000',
                    ticketNumber: 'TCK-1000',
                    vehicleNumber: 'DL-01-AA-5555',
                    ownerName: 'Amit Verma',
                    vehicleType: 'CAR',
                    allocatedSlots: 'SLOT-05',
                    entryTime: yesterday,
                    exitTime: new Date(new Date(yesterday).getTime() + 3.2 * 3600 * 1000).toISOString(),
                    billedHours: 4,
                    hourlyRate: 30,
                    totalAmount: 120,
                    paymentMethod: 'UPI',
                    paymentStatus: 'PAID',
                    paymentTime: new Date(new Date(yesterday).getTime() + 3.3 * 3600 * 1000).toISOString(),
                    transactionRef: 'TXN-UPI-884920'
                }
            ],
            users: [
                { id: 1, username: 'admin', fullName: 'System Administrator', role: 'ADMIN', createdAt: '2026-11-01 10:00:00' },
                { id: 2, username: 'operator', fullName: 'Parking Operator', role: 'OPERATOR', createdAt: '2026-11-01 10:00:00' }
            ],
            settings: {
                systemName: 'ParkEase Smart Parking System',
                maxDailyHoursCap: 8,
                currency: '₹',
                rates: { BIKE: 10, CAR: 30, TRUCK: 60 }
            }
        };
    },

    load() {
        try {
            const raw = localStorage.getItem(this.STORAGE_KEY);
            if (!raw) {
                const init = this.getInitialData();
                this.save(init);
                return init;
            }
            return JSON.parse(raw);
        } catch (e) {
            console.error('Failed to load local DB, resetting to defaults', e);
            const init = this.getInitialData();
            this.save(init);
            return init;
        }
    },

    save(data) {
        localStorage.setItem(this.STORAGE_KEY, JSON.stringify(data));
    },

    reset() {
        const init = this.getInitialData();
        this.save(init);
        return init;
    },

    // --------------------------------------------------------------------------
    // Core OOP Parking Allocation Logic
    // --------------------------------------------------------------------------
    parkVehicle(ownerName, vehicleNumber, vehicleType, contactNumber, customEntryTime) {
        const db = this.load();
        vehicleNumber = vehicleNumber.trim().toUpperCase();
        ownerName = ownerName.trim();
        vehicleType = vehicleType.toUpperCase();

        // Check if vehicle is already parked
        const existingTicket = db.tickets.find(t => t.vehicleNumber === vehicleNumber && t.status === 'ACTIVE');
        if (existingTicket) {
            throw new Error(`Vehicle ${vehicleNumber} is already parked with active ticket ${existingTicket.ticketNumber}!`);
        }

        const rateConfig = db.rates[vehicleType] || { rate: 30, bays: 1 };
        let allocatedSlotIds = [];

        if (vehicleType === 'TRUCK') {
            // Must find 2 consecutive available slots
            let found = false;
            for (let i = 0; i < db.slots.length - 1; i++) {
                if (db.slots[i].status === 'AVAILABLE' && db.slots[i + 1].status === 'AVAILABLE') {
                    allocatedSlotIds = [db.slots[i].id, db.slots[i + 1].id];
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new Error('No 2 consecutive parking bays available for Truck/Bus!');
            }
        } else {
            // Find 1 available slot
            const slot = db.slots.find(s => s.status === 'AVAILABLE');
            if (!slot) {
                throw new Error('Parking lot is full! No available slots.');
            }
            allocatedSlotIds = [slot.id];
        }

        // Generate Ticket
        const ticketSeq = db.tickets.length + 1001;
        const ticketNumber = 'TCK-' + ticketSeq;
        const entryTime = customEntryTime ? new Date(customEntryTime).toISOString() : new Date().toISOString();
        const slotNames = allocatedSlotIds.map(id => {
            const s = db.slots.find(sl => sl.id === id);
            return s ? s.slotNumber : 'SLOT-' + id;
        }).join(', ');

        // Update Slots in DB
        allocatedSlotIds.forEach(id => {
            const slot = db.slots.find(s => s.id === id);
            if (slot) {
                slot.status = 'OCCUPIED';
                slot.vehicleNumber = vehicleNumber;
                slot.ownerName = ownerName;
                slot.vehicleType = vehicleType;
                slot.ticketNumber = ticketNumber;
            }
        });

        // Add Ticket to DB
        const newTicket = {
            ticketNumber,
            vehicleNumber,
            ownerName,
            vehicleType,
            contactNumber,
            allocatedSlots: slotNames,
            slotIds: allocatedSlotIds,
            hourlyRate: rateConfig.rate,
            entryTime,
            exitTime: null,
            billedHours: 0,
            totalAmount: 0,
            status: 'ACTIVE'
        };
        db.tickets.unshift(newTicket);

        this.save(db);
        return {
            success: true,
            ticketNumber,
            vehicleNumber,
            ownerName,
            vehicleType,
            allocatedSlots: slotNames,
            entryTime,
            hourlyRate: rateConfig.rate,
            message: 'Vehicle parked successfully!'
        };
    },

    // --------------------------------------------------------------------------
    // Fee Calculation Logic (Ceiling + 8-Hour Max Cap Formula)
    // --------------------------------------------------------------------------
    calculateExit(query) {
        const db = this.load();
        const q = query.trim().toUpperCase();

        const ticket = db.tickets.find(t => 
            (t.ticketNumber.toUpperCase() === q || t.vehicleNumber.toUpperCase() === q) && t.status === 'ACTIVE'
        );

        if (!ticket) {
            throw new Error(`No active parking ticket found matching "${query}".`);
        }

        const entryDate = new Date(ticket.entryTime);
        const exitDate = new Date();
        const diffMs = exitDate.getTime() - entryDate.getTime();
        const diffMinutes = Math.max(1, Math.round(diffMs / (1000 * 60)));

        const hours = Math.floor(diffMinutes / 60);
        const mins = diffMinutes % 60;
        const formattedDuration = `${hours}h ${mins}m`;

        // Ceiling calculation for hours
        const billedHours = Math.max(1, Math.ceil(diffMinutes / 60));
        
        // 8-hour cap rule: min(billedHours, 8) * rate
        const cap = db.settings.maxDailyHoursCap || 8;
        const effectiveHours = Math.min(billedHours, cap);
        const totalAmount = effectiveHours * ticket.hourlyRate;
        const dailyCapApplied = billedHours > cap;

        return {
            ticketNumber: ticket.ticketNumber,
            vehicleNumber: ticket.vehicleNumber,
            ownerName: ticket.ownerName,
            vehicleType: ticket.vehicleType,
            allocatedSlots: ticket.allocatedSlots,
            entryTime: ticket.entryTime,
            exitTime: exitDate.toISOString(),
            formattedDuration,
            billedHours,
            hourlyRate: ticket.hourlyRate,
            totalAmount,
            dailyCapApplied
        };
    },

    // --------------------------------------------------------------------------
    // Process Exit & Generate Receipt
    // --------------------------------------------------------------------------
    processExit(ticketNumber, paymentMethod, amountPaid) {
        const db = this.load();
        const ticket = db.tickets.find(t => t.ticketNumber === ticketNumber && t.status === 'ACTIVE');

        if (!ticket) {
            throw new Error(`Active ticket ${ticketNumber} not found.`);
        }

        const exitCalc = this.calculateExit(ticketNumber);
        const exitTime = exitCalc.exitTime;

        // Free Slots
        ticket.slotIds.forEach(id => {
            const slot = db.slots.find(s => s.id === id);
            if (slot) {
                slot.status = 'AVAILABLE';
                slot.vehicleNumber = null;
                slot.ownerName = null;
                slot.vehicleType = null;
                slot.ticketNumber = null;
            }
        });

        // Update Ticket
        ticket.status = 'COMPLETED';
        ticket.exitTime = exitTime;
        ticket.billedHours = exitCalc.billedHours;
        ticket.totalAmount = amountPaid || exitCalc.totalAmount;

        // Create Receipt
        const receiptSeq = db.payments.length + 1001;
        const receiptNumber = 'RCP-' + receiptSeq;
        const paymentTime = new Date().toISOString();
        const transactionRef = 'TXN-' + (paymentMethod || 'CASH') + '-' + Math.floor(100000 + Math.random() * 900000);

        const receipt = {
            receiptNumber,
            ticketNumber: ticket.ticketNumber,
            vehicleNumber: ticket.vehicleNumber,
            ownerName: ticket.ownerName,
            vehicleType: ticket.vehicleType,
            allocatedSlots: ticket.allocatedSlots,
            entryTime: ticket.entryTime,
            exitTime,
            billedHours: ticket.billedHours,
            hourlyRate: ticket.hourlyRate,
            totalAmount: ticket.totalAmount,
            paymentMethod: paymentMethod || 'CASH',
            paymentStatus: 'PAID',
            paymentTime,
            transactionRef
        };

        db.payments.unshift(receipt);
        this.save(db);

        return receipt;
    },

    // --------------------------------------------------------------------------
    // Statistics & Reports
    // --------------------------------------------------------------------------
    getStats() {
        const db = this.load();
        const totalSlots = db.slots.length;
        const occupiedSlots = db.slots.filter(s => s.status === 'OCCUPIED').length;
        const availableSlots = totalSlots - occupiedSlots;
        const activeTickets = db.tickets.filter(t => t.status === 'ACTIVE').length;
        const totalRevenue = db.payments.reduce((acc, p) => acc + (p.totalAmount || 0), 0);

        return {
            totalSlots,
            occupiedSlots,
            availableSlots,
            activeTickets,
            totalRevenue,
            occupancyRate: Math.round((occupiedSlots / totalSlots) * 100)
        };
    }
};

window.ParkEaseDB = ParkEaseDB;
