package com.example.appointmentservice.service;

import com.example.appointmentservice.dto.AppointmentDTO;
import com.example.appointmentservice.model.Appointment;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    AppointmentDTO create(Appointment appointment);
    AppointmentDTO update(Appointment appointment, long id);
    AppointmentDTO validatePassed(long id);
    void delete(long id);
    List<AppointmentDTO> findAll();

    List<Appointment> getByPatientId(long patientId);

}
