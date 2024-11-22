package com.example.ressourceservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String type;

    private String etat;

    @ManyToOne
    private Salle salle;

    public void setId(Long id) {
        this.id = id;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }
}