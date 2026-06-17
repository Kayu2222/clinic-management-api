package com.example.demo;

import com.example.demo.model.Bed;
import com.example.demo.model.Gender;
import com.example.demo.model.Patient;
import com.example.demo.repository.BedRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.service.BedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class BedServiceTest {

    @Autowired BedService bedService;
    @Autowired BedRepository bedRepo;
    @Autowired PatientRepository patientRepo;

    @BeforeEach
    void setup() {
        // Create test bed and patient
        bedRepo.save(new Bed("TEST-B1", "TEST-R1"));
        bedRepo.save(new Bed("TEST-B2", "TEST-R1"));
        patientRepo.save(new Patient("TP001", "Test Patient", Gender.FEMALE));
    }

    @Test
    void assignPatient_setsOccupied() {
        Bed bed = bedService.assignPatient("TEST-B1", "TP001");

        assertThat(bed.isOccupied()).isTrue();
        assertThat(bed.getPatientId()).isEqualTo("TP001");

        Patient p = patientRepo.findById("TP001").orElseThrow();
        assertThat(p.getBedId()).isEqualTo("TEST-B1");
    }

    @Test
    void assignPatient_alreadyOccupied_throwsException() {
        bedService.assignPatient("TEST-B1", "TP001");

        patientRepo.save(new Patient("TP002", "Second Patient", Gender.MALE));
        assertThatThrownBy(() -> bedService.assignPatient("TEST-B1", "TP002"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already occupied");
    }

    @Test
    void releasePatient_clearsOccupant() {
        bedService.assignPatient("TEST-B1", "TP001");
        Bed released = bedService.releasePatient("TEST-B1");

        assertThat(released.isOccupied()).isFalse();
        assertThat(released.getPatientId()).isNull();

        Patient p = patientRepo.findById("TP001").orElseThrow();
        assertThat(p.getBedId()).isNull();
    }

    @Test
    void movePatient_transfersToNewBed() {
        bedService.assignPatient("TEST-B1", "TP001");
        bedService.movePatient("TEST-B1", "TEST-B2");

        Bed from = bedRepo.findById("TEST-B1").orElseThrow();
        Bed to   = bedRepo.findById("TEST-B2").orElseThrow();

        assertThat(from.isOccupied()).isFalse();
        assertThat(to.getPatientId()).isEqualTo("TP001");
    }

    @Test
    void getVacantBeds_excludesOccupied() {
        bedService.assignPatient("TEST-B1", "TP001");

        var vacant = bedService.getVacantBeds();
        assertThat(vacant).extracting(Bed::getBedId)
                .doesNotContain("TEST-B1")
                .contains("TEST-B2");
    }
}
