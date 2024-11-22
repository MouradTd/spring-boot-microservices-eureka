package com.example.patientservice.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String title;

    @NonNull
    private String attachement;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
