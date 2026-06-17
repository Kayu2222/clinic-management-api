package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @Column(name = "patient_id")
    @NotBlank(message = "Patient ID is required")
    private String patientId;

    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Gender is required (MALE or FEMALE)")
    private Gender gender;

    @Column(name = "bed_id")
    private String bedId;

    public Patient() {}

    public Patient(String patientId, String name, Gender gender) {
        this.patientId = patientId;
        this.name = name;
        this.gender = gender;
    }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public String getBedId() { return bedId; }
    public void setBedId(String bedId) { this.bedId = bedId; }
}
