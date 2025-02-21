package com.example.appointmentservice.listners;

import com.example.appointmentservice.dto.AppointmentDTO;
import com.example.appointmentservice.exception.InvalidMessageException;
import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.model.Notification;
import com.example.appointmentservice.service.AppointmentServiceImp;
import com.example.appointmentservice.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@Profile("production")
@Slf4j
public class AppointmentListener {
    private final ObjectMapper objectMapper;

    private final AppointmentServiceImp appointmentService;

    private final NotificationService notificationService;

    public AppointmentListener(
            final ObjectMapper objectMapper,
            final AppointmentServiceImp appointmentService,
            final NotificationService notificationService) {
        this.objectMapper = objectMapper;
        this.appointmentService = appointmentService;
        this.notificationService = notificationService;
    }
    @KafkaListener(topics = "appointment")
    public String listens(final String in) {


        try {
            final Map<String, Object> payload = readJsonAsMap(in);

            final Appointment appointment = appointmentFromPayload(payload);
            final AppointmentDTO savedAppointment = appointmentService.create(appointment);

            final String message = String.format(
                    "Appointment '%s' [%s] [%s] [%s] [%s] [%s] [%s] [%s] [%s]  persisted!",
                    savedAppointment.getDateTime(),
                    savedAppointment.getId(),
                    savedAppointment.getStatus(),
                    savedAppointment.getPatientId(),
                    savedAppointment.getDoctorId(),
                    savedAppointment.getDuration(),
                    savedAppointment.getNotes(),
                    savedAppointment.getSalleId(),
                    savedAppointment.getFactureId()

            );
            notificationService.publishNotification(
                    Notification.builder()
                            .message(message)
                            .timestamp(LocalDateTime.now())
                            .service("appointment-persistence")
                            .build());

        } catch(final InvalidMessageException ex) {
        }


        return in;
    }

    private Map<String, Object> readJsonAsMap(final String json) {
        try{
            final TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};
            return objectMapper.readValue(json, typeRef);
        } catch(JsonProcessingException ex) {
            throw new InvalidMessageException(ex.getMessage());
        }
    }


    private Appointment appointmentFromPayload(final Map<String, Object> payload) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return Appointment.builder()
                .dateTime(LocalDateTime.parse(payload.get("dateTime").toString(), formatter))
                .patientId(Long.parseLong(payload.get("patientId").toString()))
                .doctorId(Long.parseLong(payload.get("doctorId").toString()))
                .duration(Integer.parseInt(payload.get("duration").toString()))
                .status(payload.get("status").toString())
                .notes(payload.get("notes").toString())
                .salleId(payload.get("salleId").toString())
                .build();
    }
}
