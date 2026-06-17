package com.example.demo.controller;

import com.example.demo.dto.AssignPatientRequest;
import com.example.demo.dto.MoveBedRequest;
import com.example.demo.model.Bed;
import com.example.demo.service.BedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beds")
@Tag(name = "Beds", description = "Bed assignment, release and movement")
public class BedController {

    private final BedService bedService;

    public BedController(BedService bedService) {
        this.bedService = bedService;
    }

    @GetMapping
    @Operation(summary = "List all beds")
    public List<Bed> getAllBeds() {
        return bedService.getAllBeds();
    }

    @GetMapping("/vacant")
    @Operation(summary = "List vacant (unoccupied) beds")
    public List<Bed> getVacantBeds() {
        return bedService.getVacantBeds();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a bed by ID")
    public ResponseEntity<Bed> getBed(
            @Parameter(description = "Bed ID, e.g. W1-R1-B1") @PathVariable("id") String id) {
        return ResponseEntity.ok(bedService.getBedById(id));
    }

    @PostMapping("/{id}/assign")
    @Operation(summary = "Assign a patient to this bed")
    public ResponseEntity<Bed> assignPatient(
            @Parameter(description = "Bed ID, e.g. W1-R1-B1") @PathVariable("id") String id,
            @Valid @RequestBody AssignPatientRequest body) {
        return ResponseEntity.ok(bedService.assignPatient(id, body.getPatientId()));
    }

    @PostMapping("/{id}/release")
    @Operation(summary = "Release the patient from this bed")
    public ResponseEntity<Bed> releasePatient(
            @Parameter(description = "Bed ID, e.g. W1-R1-B1") @PathVariable("id") String id) {
        return ResponseEntity.ok(bedService.releasePatient(id));
    }

    @PostMapping("/{id}/move")
    @Operation(summary = "Move patient to another bed")
    public ResponseEntity<Void> movePatient(
            @Parameter(description = "Source bed ID, e.g. W1-R1-B1") @PathVariable("id") String id,
            @Valid @RequestBody MoveBedRequest body) {
        bedService.movePatient(id, body.getToBedId());
        return ResponseEntity.noContent().build();
    }
}
