package com.example.demo;

import com.example.demo.model.Gender;
import com.example.demo.model.Patient;
import com.example.demo.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PatientServiceTest {

    @Autowired
    PatientService patientService;

    @Test
    void registerPatient_savesAndReturns() {
        Patient p = new Patient("T001", "Test Patient", Gender.FEMALE);
        Patient saved = patientService.registerPatient(p);

        assertThat(saved.getPatientId()).isEqualTo("T001");
        assertThat(saved.getName()).isEqualTo("Test Patient");
        assertThat(saved.getGender()).isEqualTo(Gender.FEMALE);
        assertThat(saved.getBedId()).isNull();
    }

    @Test
    void registerPatient_duplicateId_throwsException() {
        Patient p = new Patient("T002", "Patient A", Gender.MALE);
        patientService.registerPatient(p);

        Patient duplicate = new Patient("T002", "Patient B", Gender.FEMALE);
        assertThatThrownBy(() -> patientService.registerPatient(duplicate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("T002");
    }

    @Test
    void getPatientById_notFound_throwsException() {
        assertThatThrownBy(() -> patientService.getPatientById("NONEXISTENT"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("NONEXISTENT");
    }

    @Test
    void dischargePatient_clearsBedId() {
        Patient p = new Patient("T003", "Patient C", Gender.MALE);
        p.setBedId("W1-R1-B1");
        patientService.registerPatient(p);

        patientService.dischargePatient("T003");

        Patient discharged = patientService.getPatientById("T003");
        assertThat(discharged.getBedId()).isNull();
    }

    @Test
    void getUnassignedPatients_returnsOnlyUnassigned() {
        patientService.registerPatient(new Patient("T004", "Unassigned", Gender.FEMALE));
        Patient assigned = new Patient("T005", "Assigned", Gender.MALE);
        assigned.setBedId("W1-R1-B1");
        patientService.registerPatient(assigned);

        List<Patient> unassigned = patientService.getUnassignedPatients();
        assertThat(unassigned).extracting(Patient::getPatientId).contains("T004").doesNotContain("T005");
    }
}
