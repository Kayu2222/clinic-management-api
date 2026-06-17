package com.example.demo;

import com.example.demo.model.ShiftAssignment;
import com.example.demo.model.Staff;
import com.example.demo.service.ShiftService;
import com.example.demo.service.StaffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ShiftServiceTest {

    @Autowired
    ShiftService shiftService;

    @Autowired
    StaffService staffService;

    // Seed a nurse and a doctor for each test (transaction rolls back after each test)
    @BeforeEach
    void setUp() {
        staffService.addStaff(new Staff("TSN01", "Nurse Test", Staff.Role.NURSE,   "1234"));
        staffService.addStaff(new Staff("TSD01", "Dr. Test",   Staff.Role.DOCTOR,  "1234"));
    }

    @Test
    void addShift_savesAndReturns() {
        ShiftAssignment sa = shiftService.addShift("TSN01", DayOfWeek.MONDAY, ShiftAssignment.Shift.MORNING);

        assertThat(sa.getId()).isNotNull();
        assertThat(sa.getStaffId()).isEqualTo("TSN01");
        assertThat(sa.getDay()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(sa.getShift()).isEqualTo(ShiftAssignment.Shift.MORNING);
    }

    @Test
    void addShift_duplicate_throwsException() {
        shiftService.addShift("TSN01", DayOfWeek.MONDAY, ShiftAssignment.Shift.MORNING);

        assertThatThrownBy(() ->
                shiftService.addShift("TSN01", DayOfWeek.MONDAY, ShiftAssignment.Shift.MORNING))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already assigned");
    }

    @Test
    void addShift_doctorHourOnNurse_throwsException() {
        assertThatThrownBy(() ->
                shiftService.addShift("TSN01", DayOfWeek.TUESDAY, ShiftAssignment.Shift.DOCTOR_HOUR))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("DOCTOR_HOUR");
    }

    @Test
    void addShift_doctorHourOnDoctor_succeeds() {
        ShiftAssignment sa = shiftService.addShift("TSD01", DayOfWeek.WEDNESDAY, ShiftAssignment.Shift.DOCTOR_HOUR);

        assertThat(sa.getShift()).isEqualTo(ShiftAssignment.Shift.DOCTOR_HOUR);
    }

    @Test
    void removeShift_removesRecord() {
        shiftService.addShift("TSN01", DayOfWeek.FRIDAY, ShiftAssignment.Shift.AFTERNOON);
        shiftService.removeShift("TSN01", DayOfWeek.FRIDAY, ShiftAssignment.Shift.AFTERNOON);

        List<ShiftAssignment> shifts = shiftService.getShiftsForStaff("TSN01");
        assertThat(shifts).isEmpty();
    }

    @Test
    void removeShift_notFound_throwsException() {
        assertThatThrownBy(() ->
                shiftService.removeShift("TSN01", DayOfWeek.MONDAY, ShiftAssignment.Shift.MORNING))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void getShiftsForDay_filtersCorrectly() {
        shiftService.addShift("TSN01", DayOfWeek.THURSDAY, ShiftAssignment.Shift.MORNING);
        shiftService.addShift("TSD01", DayOfWeek.FRIDAY,   ShiftAssignment.Shift.MORNING);

        List<ShiftAssignment> thursday = shiftService.getShiftsForDay(DayOfWeek.THURSDAY);
        assertThat(thursday).extracting(ShiftAssignment::getStaffId).containsOnly("TSN01");
    }

    @Test
    void getShiftsForStaff_filtersCorrectly() {
        shiftService.addShift("TSN01", DayOfWeek.MONDAY,    ShiftAssignment.Shift.MORNING);
        shiftService.addShift("TSN01", DayOfWeek.WEDNESDAY, ShiftAssignment.Shift.AFTERNOON);
        shiftService.addShift("TSD01", DayOfWeek.MONDAY,    ShiftAssignment.Shift.DOCTOR_HOUR);

        List<ShiftAssignment> nurseShifts = shiftService.getShiftsForStaff("TSN01");
        assertThat(nurseShifts).hasSize(2)
                .extracting(ShiftAssignment::getStaffId).containsOnly("TSN01");
    }

    @Test
    void addShift_unknownStaff_throwsException() {
        assertThatThrownBy(() ->
                shiftService.addShift("GHOST", DayOfWeek.MONDAY, ShiftAssignment.Shift.MORNING))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("GHOST");
    }
}
