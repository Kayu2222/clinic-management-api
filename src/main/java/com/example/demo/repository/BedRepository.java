package com.example.demo.repository;

import com.example.demo.model.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedRepository extends JpaRepository<Bed, String> {

    List<Bed> findByPatientIdIsNull();

    List<Bed> findByRoomId(String roomId);
}
