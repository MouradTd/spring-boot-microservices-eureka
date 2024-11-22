package com.example.patientservice.controller;


import com.example.patientservice.dto.PatientDTO;
import com.example.patientservice.model.Patient;
import com.example.patientservice.service.PatientServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/patient")
public class PatientController {
    private final PatientServiceImp patientService;
    @Autowired
    public PatientController(PatientServiceImp patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/")
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patients = patientService.findAll();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        patientService.create(patient);
        return new ResponseEntity<>(patient, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@RequestBody Patient patient, @PathVariable long id) {
        try {
            patientService.update(patient, id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update patient: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable long id) {
        try {
            patientService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete patient: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/appointements/{id}")
    public ResponseEntity<?> getAppointments(@PathVariable long id) {
        List<?> patients = patientService.getAppointments(id);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

}
