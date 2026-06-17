package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "staff")
public class Staff {

    public enum Role {
        MANAGER, DOCTOR, NURSE
    }

    @Id
    @Column(name = "staff_id")
    @NotBlank(message = "Staff ID is required")
    private String staffId;

    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Role is required (MANAGER, DOCTOR or NURSE)")
    private Role role;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 4, message = "Password must be at least 4 characters")
    private String password;

    public Staff() {}

    public Staff(String staffId, String name, Role role, String password) {
        this.staffId = staffId;
        this.name = name;
        this.role = role;
        this.password = password;
    }

    public String getStaffId() { return staffId; }
    public void setStaffId(String staffId) { this.staffId = staffId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getPassword() { return password; }
    public void setPassword(String password) {
        if (password == null || password.length() < 4)
            throw new IllegalArgumentException("Password must be at least 4 characters");
        this.password = password;
    }
}
