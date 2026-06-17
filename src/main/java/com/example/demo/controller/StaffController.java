package com.example.demo.controller;

import com.example.demo.dto.ChangePasswordRequest;
import com.example.demo.model.Staff;
import com.example.demo.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@Tag(name = "Staff", description = "Staff management (doctors, nurses, managers)")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    @Operation(summary = "List all staff")
    public List<Staff> getAllStaff() {
        return staffService.getAllStaff();
    }

    @GetMapping(params = "role")
    @Operation(summary = "List staff by role (DOCTOR, NURSE, MANAGER)")
    public List<Staff> getByRole(
            @Parameter(description = "Role: DOCTOR, NURSE, or MANAGER") @RequestParam("role") Staff.Role role) {
        return staffService.getStaffByRole(role);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a staff member by ID")
    public ResponseEntity<Staff> getStaff(
            @Parameter(description = "Staff ID, e.g. D001") @PathVariable("id") String id) {
        return ResponseEntity.ok(staffService.getStaffById(id));
    }

    @PostMapping
    @Operation(summary = "Add a new staff member")
    public ResponseEntity<Staff> addStaff(@Valid @RequestBody Staff staff) {
        return ResponseEntity.status(HttpStatus.CREATED).body(staffService.addStaff(staff));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update staff name or role")
    public ResponseEntity<Staff> updateStaff(
            @Parameter(description = "Staff ID") @PathVariable("id") String id,
            @Valid @RequestBody Staff updated) {
        return ResponseEntity.ok(staffService.updateStaff(id, updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove a staff member")
    public ResponseEntity<Void> removeStaff(
            @Parameter(description = "Staff ID") @PathVariable("id") String id) {
        staffService.removeStaff(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/password")
    @Operation(summary = "Change staff password")
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "Staff ID") @PathVariable("id") String id,
            @Valid @RequestBody ChangePasswordRequest body) {
        staffService.updatePassword(id, body.getPassword());
        return ResponseEntity.noContent().build();
    }
}
