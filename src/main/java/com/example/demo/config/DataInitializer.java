package com.example.demo.config;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Seeds default staff, patients, and beds on first startup —
 * mirroring the CareHome.sampleData() logic in the original Java app.
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(StaffRepository staffRepo,
                               PatientRepository patientRepo,
                               BedRepository bedRepo) {
        return args -> {
            // --- Staff (same defaults as CareHome.sampleData) ---
            if (staffRepo.count() == 0) {
                staffRepo.save(new Staff("D001", "Dr. Smith",  Staff.Role.DOCTOR,  "1234"));
                staffRepo.save(new Staff("N001", "Nurse Joy",  Staff.Role.NURSE,   "5678"));
                staffRepo.save(new Staff("N002", "Nurse May",  Staff.Role.NURSE,   "5678"));
                staffRepo.save(new Staff("M001", "Manager Mike", Staff.Role.MANAGER, "admin"));
            }

            // --- Patients ---
            if (patientRepo.count() == 0) {
                patientRepo.save(new Patient("P001", "Alice", Gender.FEMALE));
                patientRepo.save(new Patient("P002", "Kenn",  Gender.MALE));
                patientRepo.save(new Patient("P003", "Sam",   Gender.MALE));
            }

            // --- Beds: 2 wards × 3 rooms × 3 beds = 18 beds ---
            if (bedRepo.count() == 0) {
                for (int ward = 1; ward <= 2; ward++) {
                    for (int room = 1; room <= 3; room++) {
                        String roomId = "W" + ward + "-R" + room;
                        for (int bed = 1; bed <= 3; bed++) {
                            String bedId = roomId + "-B" + bed;
                            bedRepo.save(new Bed(bedId, roomId));
                        }
                    }
                }
            }
        };
    }
}
