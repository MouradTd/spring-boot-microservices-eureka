package com.example.appointmentservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SalleAffectationRequest {
    // Getters and Setters
    private long salleId;
    private long appointementId;
    private String status;

}
