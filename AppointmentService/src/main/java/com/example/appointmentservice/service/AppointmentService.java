package com.example.appointmentservice.service;

import com.example.appointmentservice.model.Appointment;

import java.util.List;

public interface AppointmentService {
    Appointment create(Appointment appointment);
    void update(Appointment appointment, long id);
    void delete(long id);
    List<Appointment> findAll();

    List<Appointment> getByPatientId(long patientId);

}
