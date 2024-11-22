package com.example.ressourceservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalleAffectation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Salle salle;

    private Long dentistId;

    private Long appointmentId;

    private String day;

    private LocalDateTime dateDebut;

    private LocalDateTime dateFin;
}