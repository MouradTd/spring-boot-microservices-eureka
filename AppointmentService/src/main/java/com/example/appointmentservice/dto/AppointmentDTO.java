package com.example.appointmentservice.dto;



import com.example.appointmentservice.model.Appointment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {
    private Long id;
    private LocalDateTime dateTime;
    private int duration;

    private Long patientId;

    private Long doctorId;
    private String status;
    private String notes;

    private String salleId;
    private String factureId;

    private Map<String, Object> patient;
}
