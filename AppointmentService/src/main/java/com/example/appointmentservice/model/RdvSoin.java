package com.example.appointmentservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RdvSoin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "soin_id")
    private Soin soin;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
}
