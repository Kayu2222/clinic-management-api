package com.example.demo.controller;

import com.example.demo.model.Patient;
import com.example.demo.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patients", description = "Patient registration and management")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "List all patients")
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/unassigned")
    @Operation(summary = "List patients not yet assigned to a bed")
    public List<Patient> getUnassigned() {
        return patientService.getUnassignedPatients();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a patient by ID")
    public ResponseEntity<Patient> getPatient(
            @Parameter(description = "Patient ID, e.g. P001") @PathVariable("id") String id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PostMapping
    @Operation(summary = "Register a new patient")
    public ResponseEntity<Patient> registerPatient(@Valid @RequestBody Patient patient) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.registerPatient(patient));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update patient name or gender")
    public ResponseEntity<Patient> updatePatient(
            @Parameter(description = "Patient ID, e.g. P001") @PathVariable("id") String id,
            @Valid @RequestBody Patient updated) {
        return ResponseEntity.ok(patientService.updatePatient(id, updated));
    }

    @DeleteMapping("/{id}/discharge")
    @Operation(summary = "Discharge a patient (clears bed assignment)")
    public ResponseEntity<Void> discharge(
            @Parameter(description = "Patient ID, e.g. P001") @PathVariable("id") String id) {
        patientService.dischargePatient(id);
        return ResponseEntity.noContent().build();
    }
}
