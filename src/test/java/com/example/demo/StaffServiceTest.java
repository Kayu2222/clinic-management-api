package com.example.demo;

import com.example.demo.model.Staff;
import com.example.demo.service.StaffService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class StaffServiceTest {

    @Autowired
    StaffService staffService;

    @Test
    void addStaff_savesAndReturns() {
        Staff s = new Staff("TS001", "Dr. Test", Staff.Role.DOCTOR, "abcd");
        Staff saved = staffService.addStaff(s);

        assertThat(saved.getStaffId()).isEqualTo("TS001");
        assertThat(saved.getRole()).isEqualTo(Staff.Role.DOCTOR);
    }

    @Test
    void addStaff_duplicateId_throwsException() {
        staffService.addStaff(new Staff("TS002", "Nurse A", Staff.Role.NURSE, "1234"));

        assertThatThrownBy(() -> staffService.addStaff(new Staff("TS002", "Nurse B", Staff.Role.NURSE, "5678")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("TS002");
    }

    @Test
    void getStaffByRole_filtersCorrectly() {
        staffService.addStaff(new Staff("TS003", "Dr. A", Staff.Role.DOCTOR, "1234"));
        staffService.addStaff(new Staff("TS004", "Nurse B", Staff.Role.NURSE, "1234"));

        List<Staff> doctors = staffService.getStaffByRole(Staff.Role.DOCTOR);
        assertThat(doctors).extracting(Staff::getStaffId).contains("TS003").doesNotContain("TS004");
    }

    @Test
    void removeStaff_deletesRecord() {
        staffService.addStaff(new Staff("TS005", "Manager X", Staff.Role.MANAGER, "admin"));
        staffService.removeStaff("TS005");

        assertThatThrownBy(() -> staffService.getStaffById("TS005"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updatePassword_tooShort_throwsException() {
        staffService.addStaff(new Staff("TS006", "Nurse C", Staff.Role.NURSE, "1234"));

        assertThatThrownBy(() -> staffService.updatePassword("TS006", "ab"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("4");
    }
}
