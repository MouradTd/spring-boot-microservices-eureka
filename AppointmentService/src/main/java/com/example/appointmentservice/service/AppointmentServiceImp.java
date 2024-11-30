package com.example.appointmentservice.service;

import com.example.appointmentservice.model.Appointment;

import com.example.appointmentservice.repository.AppointmentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Service
@AllArgsConstructor
public class AppointmentServiceImp implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    public AppointmentServiceImp(KafkaTemplate<String, String> kafkaTemplate, AppointmentRepository appointmentRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.appointmentRepository = appointmentRepository;
    }
    @Override
    public Appointment create(Appointment appointment) {
         Appointment newAppointment = appointmentRepository.save(appointment);
        sendAppointmentNotification("New Appointment created for patient with id: " + newAppointment.getPatientId()
                + " here's all the info " + newAppointment.toString());

        return newAppointment;
    }

    @Override
    public void update(Appointment appointment, long id) {
        Appointment existingAppointement = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        for (Field field : Appointment.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (!field.getName().equals("id")) { // Exclude the id field
                    Object newValue = field.get(appointment);
                    if (newValue != null) {
                        field.set(existingAppointement, newValue);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to update field: " + field.getName(), e);
            }
        }
        appointmentRepository.save(existingAppointement);

    }

    @Override
    public void delete(long id) {
        Appointment existingAppointement = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        if (existingAppointement.getStatus().equalsIgnoreCase("pending")) {
            appointmentRepository.delete(existingAppointement);
        }else{
            throw new RuntimeException("Appointment can't be deleted");
        }
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public List<Appointment> getByPatientId(long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }


    public void sendAppointmentNotification(String message) {
        kafkaTemplate.send("appointment-topic", message);
    }
}
