package com.example.appointmentservice.service;

import com.example.appointmentservice.dto.AppointmentDTO;
import com.example.appointmentservice.dto.SalleAffectationRequest;
import com.example.appointmentservice.model.Appointment;

import com.example.appointmentservice.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
@AllArgsConstructor
public class AppointmentServiceImp implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final WebClient.Builder webClient;
    private static final Logger logger = LoggerFactory.getLogger(AppointmentServiceImp.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    public AppointmentServiceImp(KafkaTemplate<String, String> kafkaTemplate, AppointmentRepository appointmentRepository, WebClient.Builder webClient) {
        this.kafkaTemplate = kafkaTemplate;
        this.appointmentRepository = appointmentRepository;
        this.webClient = webClient;
    }
    @Override
    public AppointmentDTO create(Appointment appointment) {
         Appointment newAppointment = appointmentRepository.save(appointment);
        sendAppointmentNotification("New Appointment created for patient with id: " + newAppointment.getPatientId()
                + " here's all the info " + newAppointment.toString());
        AppointmentDTO appointmentDTO = convertToDTO(newAppointment);
        try {
            Map<String, Object> patient = webClient.build()
                    .get()
                    .uri("http://patient-service/api/patient/" + appointmentDTO.getPatientId())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
            appointmentDTO.setPatient(patient);
        } catch (Exception e) {
            logger.error("Error fetching patient object {}: {}", appointment.getPatientId(), e.getMessage(), e);
            throw new BadRequestException("Failed to fetch patient details: " + e.getMessage());
        }

        return appointmentDTO;
    }

    @Override
    public AppointmentDTO update(Appointment appointment, long id) {
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
        AppointmentDTO appointmentDTO = convertToDTO(existingAppointement);
        try {
            Map<String, Object> patient = webClient.build()
                    .get()
                    .uri("http://patient-service/api/patient/" + appointmentDTO.getPatientId())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
            appointmentDTO.setPatient(patient);
        } catch (Exception e) {
            logger.error("Error fetching patient object {}: {}", appointment.getPatientId(), e.getMessage(), e);
            throw new BadRequestException("Failed to fetch patient details: " + e.getMessage());
        }
        return appointmentDTO;
    }

    @Override
    public void delete(long id) {
        Appointment existingAppointement = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        if (existingAppointement.getStatus().equalsIgnoreCase("pending")) {
//            appointmentRepository.delete(existingAppointement);
            appointmentRepository.deleteById(id);
//            sendAppointmentNotification("An Appointment was deleted for patient with id: " + existingAppointement.getPatientId()
//                    + " here's all the info " + existingAppointement.toString());
        }else{
            throw new RuntimeException("Appointment can't be deleted");
        }
    }

    @Override
    @CircuitBreaker(name = "patientService", fallbackMethod = "fallbackGetPatient")
    @Retry(name = "patientService")
    public List<AppointmentDTO> findAll() {
        List<Appointment> appointments = appointmentRepository.findAll();
        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        for (AppointmentDTO appointment : appointmentDTOs) {
            try {
                Map<String, Object> patient = webClient.build()
                        .get()
                        .uri("http://patient-service/api/patient/" + appointment.getPatientId())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                        .block();
                appointment.setPatient(patient);
            } catch (Exception e) {
                logger.error("Error fetching patient object {}: {}", appointment.getPatientId(), e.getMessage(), e);
                throw new BadRequestException("Failed to fetch patient details: " + e.getMessage());
            }
        }
        return appointmentDTOs;
    }
    public List<AppointmentDTO> fallbackGetPatient(Throwable t) {
        logger.error("Fallback method called due to: {}", t.getMessage());
        // Handle the fallback logic here, e.g., return an empty list or default values
        return Collections.emptyList();
    }

    @Override
    public List<Appointment> getByPatientId(long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    @Override
    @Transactional
    @Retryable(
            value = {ResourceAccessException.class, HttpStatusCodeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public AppointmentDTO validatePassed(long id) {
        Appointment existingAppointement = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        existingAppointement.setStatus("passed");
            appointmentRepository.save(existingAppointement);

            // Call to resource service
            SalleAffectationRequest request = new SalleAffectationRequest();
            request.setAppointementId(existingAppointement.getId());
            request.setSalleId(Long.parseLong(existingAppointement.getSalleId()));
            request.setStatus("passed");
            logger.info("Sending request to ressource-service: {}", request);
            // Set necessary fields in request
            try {
                restTemplate.postForObject("http://ressource-service/api/ressource/salle/validate", request, Response.class);
            } catch (HttpStatusCodeException e) {
                logger.error("HTTP Status Code Exception: {}", e.getStatusCode());
                throw new RuntimeException("Failed to update appointment: " + e.getMessage(), e);
            } catch (ResourceAccessException e) {
                logger.error("Resource Access Exception: {}", e.getMessage());
                throw new RuntimeException("Failed to update appointment: " + e.getMessage(), e);
            } catch (Exception e) {
                logger.error("Exception: {}", e.getMessage());
                throw new RuntimeException("Failed to update appointment: " + e.getMessage(), e);
            }
        AppointmentDTO appointmentDTO = convertToDTO(existingAppointement);
        try {
            Map<String, Object> patient = webClient.build()
                    .get()
                    .uri("http://patient-service/api/patient/" + appointmentDTO.getPatientId())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
            appointmentDTO.setPatient(patient);
        } catch (Exception e) {
            logger.error("Error fetching patient object {}: {}", existingAppointement.getPatientId(), e.getMessage(), e);
            throw new BadRequestException("Failed to fetch patient details: " + e.getMessage());
        }

        return appointmentDTO;
    }


    public void sendAppointmentNotification(String message) {
        kafkaTemplate.send("appointment-topic", message);
    }


    private AppointmentDTO convertToDTO(Appointment appointment) {
        AppointmentDTO appointementDTO = new AppointmentDTO();
        BeanUtils.copyProperties(appointment, appointementDTO);
        return appointementDTO;
    }
}
