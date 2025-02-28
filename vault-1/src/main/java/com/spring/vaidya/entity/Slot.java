package com.spring.vaidya.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Schema(description = "Entity representing a Slot for doctor appointments.")
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the slot", example = "1")
    private Long slotId;

    @Schema(description = "Start time of the slot", example = "09:00:00")
    private LocalTime startTime;

    @Schema(description = "End time of the slot", example = "09:30:00")
    private LocalTime endTime;

    @Schema(description = "Duration or time range of the slot", example = "30 minutes")
    private String slotRange;

    @Schema(description = "Status of the slot availability", example = "yes")
    private String status = "yes"; // Default value indicating availability

    @Schema(description = "Date for which the slot is available", example = "2024-02-25")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @Schema(description = "Doctor associated with the slot")
    private User doctor;

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getSlotRange() {
        return slotRange;
    }

    public void setSlotRange(String slotRange) {
        this.slotRange = slotRange;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, doctor, endTime, slotId, slotRange, startTime, status);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Slot other = (Slot) obj;
        return Objects.equals(date, other.date) && Objects.equals(doctor, other.doctor)
                && Objects.equals(endTime, other.endTime) && Objects.equals(slotId, other.slotId)
                && Objects.equals(slotRange, other.slotRange) && Objects.equals(startTime, other.startTime)
                && Objects.equals(status, other.status);
    }

    @Override
    public String toString() {
        return "Slot [slotId=" + slotId + ", startTime=" + startTime + ", endTime=" + endTime + ", slotRange="
                + slotRange + ", status=" + status + ", date=" + date + ", doctor=" + doctor + "]";
    }

    public Slot(Long slotId, LocalTime startTime, LocalTime endTime, String slotRange, String status, LocalDate date,
                User doctor) {
        super();
        this.slotId = slotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotRange = slotRange;
        this.status = status;
        this.date = date;
        this.doctor = doctor;
    }

    public Slot() {
        super();
    }
}
