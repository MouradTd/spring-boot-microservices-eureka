package com.example.patientservice.controller;


import com.example.patientservice.dto.PatientDTO;
import com.example.patientservice.model.Patient;
import com.example.patientservice.service.PatientServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Patient", description = "the Patient Api")
@RestController
@RequestMapping("/api/patient")
public class PatientController {
    private final PatientServiceImp patientService;
    @Autowired
    public PatientController(PatientServiceImp patientService) {
        this.patientService = patientService;
    }

    @Operation(
            summary = "Fetch all patients",
            description = "fetches all patients from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @GetMapping("/")
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patients = patientService.findAll();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @Operation(
            summary = "Create a patient",
            description = "Create a patient in the database")
    @PostMapping("/")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        patientService.create(patient);
        return new ResponseEntity<>(patient, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update a patient",
            description = "Update a patient in the database")
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@RequestBody Patient patient, @PathVariable long id) {
        try {
            patientService.update(patient, id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update patient: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Delete a patient",
            description = "Delete a patient from the database")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable long id) {
        try {
            patientService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete patient: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Fetch a patients appointments",
            description = "Fetch a patients appointements from the database")
    @GetMapping("/appointements/{id}")
    public ResponseEntity<PatientDTO> getAppointments(@PathVariable long id) {
        PatientDTO patients = patientService.getAppointments(id);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

}
