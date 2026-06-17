package com.example.demo.service;

import com.example.demo.model.Staff;
import com.example.demo.repository.StaffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(noRollbackFor = {IllegalArgumentException.class, IllegalStateException.class})
public class StaffService {

    private final StaffRepository staffRepo;

    public StaffService(StaffRepository staffRepo) {
        this.staffRepo = staffRepo;
    }

    public List<Staff> getAllStaff() {
        return staffRepo.findAll();
    }

    public Staff getStaffById(String staffId) {
        return staffRepo.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found: " + staffId));
    }

    public List<Staff> getStaffByRole(Staff.Role role) {
        return staffRepo.findByRole(role);
    }

    public Staff addStaff(Staff staff) {
        if (staffRepo.existsById(staff.getStaffId())) {
            throw new IllegalArgumentException("Staff ID already exists: " + staff.getStaffId());
        }
        return staffRepo.save(staff);
    }

    public Staff updateStaff(String staffId, Staff updated) {
        Staff existing = getStaffById(staffId);
        existing.setName(updated.getName());
        existing.setRole(updated.getRole());
        return staffRepo.save(existing);
    }

    public void removeStaff(String staffId) {
        if (!staffRepo.existsById(staffId)) {
            throw new IllegalArgumentException("Staff not found: " + staffId);
        }
        staffRepo.deleteById(staffId);
    }

    public Staff updatePassword(String staffId, String newPassword) {
        Staff s = getStaffById(staffId);
        s.setPassword(newPassword); // validation is in setter
        return staffRepo.save(s);
    }
}
