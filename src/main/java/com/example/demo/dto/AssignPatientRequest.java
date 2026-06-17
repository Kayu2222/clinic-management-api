package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body for POST /api/beds/{id}/assign.
 * Example: { "patientId": "P001" }
 */
public class AssignPatientRequest {

    @NotBlank(message = "patientId is required")
    private String patientId;

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
}
