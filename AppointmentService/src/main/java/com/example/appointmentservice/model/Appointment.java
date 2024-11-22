package com.example.appointmentservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateTime;
    private int duration; // in minutes
    @NonNull
    private Long patientId;
    @NonNull
    private Long doctorId;
    private String status;
    private String notes;
    @NonNull
    private String salleId;
    private String factureId;
}