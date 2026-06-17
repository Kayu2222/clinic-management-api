package com.example.demo.service;

import com.example.demo.model.Bed;
import com.example.demo.model.Patient;
import com.example.demo.repository.BedRepository;
import com.example.demo.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(noRollbackFor = {IllegalArgumentException.class, IllegalStateException.class})
public class BedService {

    private final BedRepository bedRepo;
    private final PatientRepository patientRepo;

    public BedService(BedRepository bedRepo, PatientRepository patientRepo) {
        this.bedRepo = bedRepo;
        this.patientRepo = patientRepo;
    }

    public List<Bed> getAllBeds() {
        return bedRepo.findAll();
    }

    public List<Bed> getVacantBeds() {
        return bedRepo.findByPatientIdIsNull();
    }

    public Bed getBedById(String bedId) {
        return bedRepo.findById(bedId)
                .orElseThrow(() -> new IllegalArgumentException("Bed not found: " + bedId));
    }

    /**
     * Assign an existing patient to a vacant bed.
     * Mirrors BedService.assignPatientToBed() from the original Java app.
     */
    public Bed assignPatient(String bedId, String patientId) {
        Bed bed = getBedById(bedId);
        if (bed.isOccupied()) {
            throw new IllegalStateException("Bed " + bedId + " is already occupied");
        }

        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + patientId));
        if (patient.getBedId() != null) {
            throw new IllegalStateException("Patient " + patientId + " is already in bed " + patient.getBedId());
        }

        bed.setPatientId(patientId);
        patient.setBedId(bedId);
        patientRepo.save(patient);
        return bedRepo.save(bed);
    }

    /**
     * Release a patient from a bed (discharge / vacate).
     * Mirrors BedService.releasePatientFromBed() from the original Java app.
     */
    public Bed releasePatient(String bedId) {
        Bed bed = getBedById(bedId);
        if (!bed.isOccupied()) {
            throw new IllegalStateException("Bed " + bedId + " is already vacant");
        }

        String patientId = bed.getPatientId();
        patientRepo.findById(patientId).ifPresent(p -> {
            p.setBedId(null);
            patientRepo.save(p);
        });

        bed.setPatientId(null);
        return bedRepo.save(bed);
    }

    /**
     * Move a patient from one bed to another.
     * Mirrors BedService.movePatientToAnotherBed() from the original Java app.
     */
    public void movePatient(String fromBedId, String toBedId) {
        Bed from = getBedById(fromBedId);
        Bed to = getBedById(toBedId);

        if (!from.isOccupied()) throw new IllegalStateException("Source bed " + fromBedId + " is vacant");
        if (to.isOccupied())    throw new IllegalStateException("Target bed " + toBedId + " is occupied");

        String patientId = from.getPatientId();
        from.setPatientId(null);
        to.setPatientId(patientId);

        patientRepo.findById(patientId).ifPresent(p -> {
            p.setBedId(toBedId);
            patientRepo.save(p);
        });

        bedRepo.save(from);
        bedRepo.save(to);
    }
}
