package com.example.demo.repository;

import com.example.demo.model.ShiftAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<ShiftAssignment, Long> {

    List<ShiftAssignment> findByStaffId(String staffId);

    List<ShiftAssignment> findByDay(DayOfWeek day);

    Optional<ShiftAssignment> findByStaffIdAndDayAndShift(
            String staffId, DayOfWeek day, ShiftAssignment.Shift shift);
}
