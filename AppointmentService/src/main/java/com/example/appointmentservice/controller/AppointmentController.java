package com.example.appointmentservice.controller;


import com.example.appointmentservice.service.AppointmentServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {
    private final AppointmentServiceImp appointmentService;
    @Autowired
    public AppointmentController(AppointmentServiceImp appointmentService) {
        this.appointmentService = appointmentService;
    }


    @GetMapping("/patient/{id}")
    public ResponseEntity<?> getByPatientId(@PathVariable long id) {
        List<?> patients = appointmentService.getByPatientId(id);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }
}
