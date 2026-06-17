package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request body for POST /api/shifts and DELETE /api/shifts.
 * Example: { "staffId": "N001", "day": "MONDAY", "shift": "MORNING" }
 * Valid day values  : MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
 * Valid shift values: MORNING, AFTERNOON, DOCTOR_HOUR
 */
public class ShiftRequest {

    @NotBlank(message = "staffId is required")
    private String staffId;

    @NotBlank(message = "day is required (e.g. MONDAY)")
    private String day;

    @NotBlank(message = "shift is required (MORNING, AFTERNOON, DOCTOR_HOUR)")
    private String shift;

    public String getStaffId() { return staffId; }
    public void setStaffId(String staffId) { this.staffId = staffId; }

    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }
}
