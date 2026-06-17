package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "beds")
public class Bed {

    @Id
    @Column(name = "bed_id")
    private String bedId;

    @Column(name = "room_id", nullable = false)
    private String roomId;

    /** patientId of the current occupant, null if vacant */
    @Column(name = "patient_id")
    private String patientId;

    public Bed() {}

    public Bed(String bedId, String roomId) {
        this.bedId = bedId;
        this.roomId = roomId;
    }

    public String getBedId() { return bedId; }
    public void setBedId(String bedId) { this.bedId = bedId; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public boolean isOccupied() { return patientId != null; }
}
