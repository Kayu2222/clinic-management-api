package com.example.demo.model;

import jakarta.persistence.*;
import java.time.DayOfWeek;

@Entity
@Table(name = "shift_assignments",
       uniqueConstraints = @UniqueConstraint(columnNames = {"staff_id", "shift_day", "shift"}))
public class ShiftAssignment {

    public enum Shift {
        MORNING, AFTERNOON, DOCTOR_HOUR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "staff_id", nullable = false)
    private String staffId;

    @Enumerated(EnumType.STRING)
    @Column(name = "shift_day", nullable = false)
    private DayOfWeek day;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Shift shift;

    public ShiftAssignment() {}

    public ShiftAssignment(String staffId, DayOfWeek day, Shift shift) {
        this.staffId = staffId;
        this.day = day;
        this.shift = shift;
    }

    public Long getId() { return id; }

    public String getStaffId() { return staffId; }
    public void setStaffId(String staffId) { this.staffId = staffId; }

    public DayOfWeek getDay() { return day; }
    public void setDay(DayOfWeek day) { this.day = day; }

    public Shift getShift() { return shift; }
    public void setShift(Shift shift) { this.shift = shift; }
}
