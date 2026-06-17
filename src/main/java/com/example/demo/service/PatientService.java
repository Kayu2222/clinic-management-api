package com.example.demo.service;

import com.example.demo.model.Patient;
import com.example.demo.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(noRollbackFor = {IllegalArgumentException.class, IllegalStateException.class})
public class PatientService {

    private final PatientRepository patientRepo;

    public PatientService(PatientRepository patientRepo) {
        this.patientRepo = patientRepo;
    }

    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    public Patient getPatientById(String patientId) {
        return patientRepo.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + patientId));
    }

    public Patient registerPatient(Patient patient) {
        if (patientRepo.existsById(patient.getPatientId())) {
            throw new IllegalArgumentException("Patient already exists: " + patient.getPatientId());
        }
        return patientRepo.save(patient);
    }

    public Patient updatePatient(String patientId, Patient updated) {
        Patient existing = getPatientById(patientId);
        existing.setName(updated.getName());
        existing.setGender(updated.getGender());
        return patientRepo.save(existing);
    }

    public void dischargePatient(String patientId) {
        Patient p = getPatientById(patientId);
        p.setBedId(null);
        patientRepo.save(p);
    }

    public List<Patient> getUnassignedPatients() {
        return patientRepo.findByBedIdIsNull();
    }
}
