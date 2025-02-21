package com.example.ressourceservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SalleAffectationRequest {
    // Getters and setters
    private Long appointementId;
    private Long salleId;
    private String status;

}
