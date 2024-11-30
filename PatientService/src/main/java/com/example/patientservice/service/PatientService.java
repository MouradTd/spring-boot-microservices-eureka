package com.example.patientservice.service;

import com.example.patientservice.dto.PatientDTO;
import com.example.patientservice.model.Patient;

import java.util.List;

public interface PatientService {
    void create(Patient patient);
    void update(Patient patient, long id);
    void delete(long id);
    List<PatientDTO> findAll();

    PatientDTO getAppointments(long patientId);
}
