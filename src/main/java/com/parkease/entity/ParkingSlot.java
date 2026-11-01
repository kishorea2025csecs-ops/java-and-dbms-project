package com.parkease.entity;

import com.parkease.constant.SlotStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "parking_slots", indexes = {
    @Index(name = "idx_slots_status", columnList = "status")
})
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id")
    private Long slotId;

    @Column(name = "slot_number", nullable = false, unique = true, length = 20)
    private String slotNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SlotStatus status = SlotStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "current_vehicle_id", foreignKey = @ForeignKey(name = "fk_slot_vehicle"))
    private VehicleEntity currentVehicle;

    public ParkingSlot() {}

    public ParkingSlot(String slotNumber) {
        this.slotNumber = slotNumber;
        this.status = SlotStatus.AVAILABLE;
        this.currentVehicle = null;
    }

    public ParkingSlot(Integer slotNumberInt) {
        this.slotNumber = String.format("SLOT-%02d", slotNumberInt);
        this.status = SlotStatus.AVAILABLE;
        this.currentVehicle = null;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public SlotStatus getStatus() {
        return status;
    }

    public void setStatus(SlotStatus status) {
        this.status = status;
    }

    public VehicleEntity getCurrentVehicle() {
        return currentVehicle;
    }

    public void setCurrentVehicle(VehicleEntity currentVehicle) {
        this.currentVehicle = currentVehicle;
    }

    /**
     * Extracts numeric value (e.g. "SLOT-05" -> 5) for sorting and consecutive bay math.
     */
    public int getNumericSlotNumber() {
        if (slotNumber == null) return slotId != null ? slotId.intValue() : 0;
        try {
            String digits = slotNumber.replaceAll("[^0-9]", "");
            return digits.isEmpty() ? (slotId != null ? slotId.intValue() : 0) : Integer.parseInt(digits);
        } catch (Exception e) {
            return slotId != null ? slotId.intValue() : 0;
        }
    }

    public String getDisplayNumber() {
        return slotNumber != null ? slotNumber.toUpperCase() : "SLOT-00";
    }
}
