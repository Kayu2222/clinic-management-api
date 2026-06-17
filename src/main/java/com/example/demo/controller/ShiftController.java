package com.example.demo.controller;

import com.example.demo.dto.ShiftRequest;
import com.example.demo.model.ShiftAssignment;
import com.example.demo.service.ShiftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/api/shifts")
@Tag(name = "Shifts", description = "Staff shift roster management")
public class ShiftController {

    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping
    @Operation(summary = "List shift assignments",
               description = "No params → all shifts. staffId → filter by staff. day → filter by day (MONDAY–SUNDAY). Use only one filter at a time.")
    public List<ShiftAssignment> getShifts(
            @Parameter(description = "Staff ID, e.g. N001 (optional)") @RequestParam(value = "staffId", required = false) String staffId,
            @Parameter(description = "Day of week, e.g. MONDAY (optional)")  @RequestParam(value = "day",     required = false) DayOfWeek day) {
        if (staffId != null) return shiftService.getShiftsForStaff(staffId);
        if (day     != null) return shiftService.getShiftsForDay(day);
        return shiftService.getAllShifts();
    }

    @PostMapping
    @Operation(summary = "Assign a shift",
               description = "day: MONDAY–SUNDAY  |  shift: MORNING, AFTERNOON, DOCTOR_HOUR (doctors only)")
    public ResponseEntity<ShiftAssignment> addShift(@Valid @RequestBody ShiftRequest body) {
        DayOfWeek day = DayOfWeek.valueOf(body.getDay().toUpperCase());
        ShiftAssignment.Shift shift = ShiftAssignment.Shift.valueOf(body.getShift().toUpperCase());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shiftService.addShift(body.getStaffId(), day, shift));
    }

    @DeleteMapping
    @Operation(summary = "Remove a shift assignment",
               description = "day: MONDAY–SUNDAY  |  shift: MORNING, AFTERNOON, DOCTOR_HOUR")
    public ResponseEntity<Void> removeShift(@Valid @RequestBody ShiftRequest body) {
        DayOfWeek day = DayOfWeek.valueOf(body.getDay().toUpperCase());
        ShiftAssignment.Shift shift = ShiftAssignment.Shift.valueOf(body.getShift().toUpperCase());
        shiftService.removeShift(body.getStaffId(), day, shift);
        return ResponseEntity.noContent().build();
    }
}
