package com.parkease.model;

import com.parkease.constant.SlotStatus;
import com.parkease.exception.ParkingFullException;
import java.util.*;

/**
 * ParkingLot Class demonstrating Object-Oriented Principles:
 * - Encapsulation (managed internal state of 20 slots)
 * - Collections (List, Map)
 * - Static Constants
 * - Consecutive Slot Allocation Algorithm for Trucks
 */
public class ParkingLot {

    public static final int TOTAL_SLOTS = 20;

    /**
     * Inner class representing an individual slot in the OOP model
     */
    public static class SlotItem {
        private final int slotNumber;
        private SlotStatus status;
        private Vehicle parkedVehicle;

        public SlotItem(int slotNumber) {
            this.slotNumber = slotNumber;
            this.status = SlotStatus.AVAILABLE;
            this.parkedVehicle = null;
        }

        public int getSlotNumber() {
            return slotNumber;
        }

        public SlotStatus getStatus() {
            return status;
        }

        public void setStatus(SlotStatus status) {
            this.status = status;
        }

        public Vehicle getParkedVehicle() {
            return parkedVehicle;
        }

        public void setParkedVehicle(Vehicle parkedVehicle) {
            this.parkedVehicle = parkedVehicle;
        }

        public boolean isAvailable() {
            return this.status == SlotStatus.AVAILABLE;
        }
    }

    private final List<SlotItem> slots;

    public ParkingLot() {
        this(TOTAL_SLOTS);
    }

    public ParkingLot(int capacity) {
        this.slots = new ArrayList<>(capacity);
        for (int i = 1; i <= capacity; i++) {
            slots.add(new SlotItem(i));
        }
    }

    /**
     * Finds the first available slot or consecutive slots for the given vehicle.
     * Demonstrates Algorithm Logic and Collections.
     * 
     * @param vehicle the vehicle requesting allocation
     * @return List of allocated slot numbers
     * @throws ParkingFullException if insufficient slots are found
     */
    public synchronized List<Integer> findAndAllocateSlots(Vehicle vehicle) {
        int required = vehicle.getRequiredSlots();
        List<Integer> allocated = new ArrayList<>();

        if (required == 1) {
            // Find first available single slot
            for (SlotItem slot : slots) {
                if (slot.isAvailable()) {
                    slot.setStatus(SlotStatus.OCCUPIED);
                    slot.setParkedVehicle(vehicle);
                    allocated.add(slot.getSlotNumber());
                    return allocated;
                }
            }
            throw new ParkingFullException("Parking Lot is full! No available slots for " + vehicle.getVehicleType().getDisplayName());
        } else if (required == 2) {
            // Find 2 consecutive available slots (e.g. slots 1 & 2, or 3 & 4)
            for (int i = 0; i < slots.size() - 1; i++) {
                SlotItem s1 = slots.get(i);
                SlotItem s2 = slots.get(i + 1);

                if (s1.isAvailable() && s2.isAvailable()) {
                    s1.setStatus(SlotStatus.OCCUPIED);
                    s1.setParkedVehicle(vehicle);
                    s2.setStatus(SlotStatus.OCCUPIED);
                    s2.setParkedVehicle(vehicle);
                    allocated.add(s1.getSlotNumber());
                    allocated.add(s2.getSlotNumber());
                    return allocated;
                }
            }
            throw new ParkingFullException("Cannot park Truck: No two consecutive parking slots available!");
        }

        throw new IllegalArgumentException("Unsupported slot requirement: " + required);
    }

    /**
     * Releases the given slot numbers.
     */
    public synchronized void releaseSlots(List<Integer> slotNumbers) {
        for (Integer num : slotNumbers) {
            if (num != null && num >= 1 && num <= slots.size()) {
                SlotItem slot = slots.get(num - 1);
                slot.setStatus(SlotStatus.AVAILABLE);
                slot.setParkedVehicle(null);
            }
        }
    }

    public List<SlotItem> getSlots() {
        return Collections.unmodifiableList(slots);
    }
}

