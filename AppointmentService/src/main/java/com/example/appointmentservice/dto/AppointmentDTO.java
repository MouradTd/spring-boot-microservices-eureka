package com.example.appointmentservice.dto;



import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentDTO {
    private LocalDateTime dateTime;
    private int duration;

    private Long patientId;

    private Long doctorId;
    private String status;
    private String notes;

    private String salleId;
    private String factureId;
}
