package com.example.appointmentservice.controller;


import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.service.AppointmentServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Appointment", description = "the Appointment Api")
@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {
    private final AppointmentServiceImp appointmentService;
    @Autowired
    public AppointmentController(AppointmentServiceImp appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Operation(
            summary = "Fetch all appointments",
            description = "fetches all appointments from the database")
    @GetMapping("/")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.findAll();
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @Operation(
            summary = "Fetch all appointments by patient id",
            description = "fetches all appointments from the database by patient id")
    @GetMapping("/patient/{id}")
    public ResponseEntity<?> getByPatientId(@PathVariable long id) {
        List<?> appointments = appointmentService.getByPatientId(id);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @Operation(
            summary = "Create an appointment",
            description = "Create an appointment in the database")
    @PostMapping("/")
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        appointmentService.create(appointment);
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update an appointment",
            description = "Update an appointment in the database")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(@RequestBody Appointment appointment, @PathVariable long id) {
        try {
            appointmentService.update(appointment, id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update appointment: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Delete an appointment",
            description = "Delete an appointment from the database")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable long id) {
        try {
            appointmentService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete appointment: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
