package com.example.appointmentservice.service;

import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.repository.AppointmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AppointmentServiceImp implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    @Override
    public void create(Appointment appointment) {

    }

    @Override
    public void update(Appointment appointment, long id) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public List<Appointment> findAll() {
        return null;
    }

    @Override
    public List<Appointment> getByPatientId(long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }
}
