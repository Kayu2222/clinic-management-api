package com.example.demo.service;

import com.example.demo.model.ShiftAssignment;
import com.example.demo.model.Staff;
import com.example.demo.repository.ShiftRepository;
import com.example.demo.repository.StaffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;

@Service
@Transactional(noRollbackFor = {IllegalArgumentException.class, IllegalStateException.class})
public class ShiftService {

    private final ShiftRepository shiftRepo;
    private final StaffRepository staffRepo;

    public ShiftService(ShiftRepository shiftRepo, StaffRepository staffRepo) {
        this.shiftRepo = shiftRepo;
        this.staffRepo = staffRepo;
    }

    public List<ShiftAssignment> getShiftsForStaff(String staffId) {
        return shiftRepo.findByStaffId(staffId);
    }

    public List<ShiftAssignment> getShiftsForDay(DayOfWeek day) {
        return shiftRepo.findByDay(day);
    }

    public List<ShiftAssignment> getAllShifts() {
        return shiftRepo.findAll();
    }

    /**
     * Assign a shift to a staff member.
     * Mirrors RosterService.addShift() from the original Java app.
     */
    public ShiftAssignment addShift(String staffId, DayOfWeek day, ShiftAssignment.Shift shift) {
        Staff staff = staffRepo.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found: " + staffId));

        // DOCTOR_HOUR is only for doctors
        if (shift == ShiftAssignment.Shift.DOCTOR_HOUR && staff.getRole() != Staff.Role.DOCTOR) {
            throw new IllegalStateException("DOCTOR_HOUR shift is only assignable to doctors");
        }

        boolean exists = shiftRepo.findByStaffIdAndDayAndShift(staffId, day, shift).isPresent();
        if (exists) {
            throw new IllegalStateException("Shift already assigned: " + staffId + " " + day + " " + shift);
        }

        return shiftRepo.save(new ShiftAssignment(staffId, day, shift));
    }

    /**
     * Remove a shift assignment.
     * Mirrors RosterService.removeShift() from the original Java app.
     */
    public void removeShift(String staffId, DayOfWeek day, ShiftAssignment.Shift shift) {
        ShiftAssignment sa = shiftRepo.findByStaffIdAndDayAndShift(staffId, day, shift)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Shift not found: " + staffId + " " + day + " " + shift));
        shiftRepo.delete(sa);
    }
}
