package com.example.notificationservice.service;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class NotificationServiceImp  {
    private final WebClient.Builder webClient;
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImp.class);
    public NotificationServiceImp(WebClient.Builder webClient) {
        this.webClient = webClient;

    }
    @KafkaListener(topics = "appointment-topic", groupId = "notification-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
        try {
            // Extract salleId and appointmentId from the message string
            String salleId = extractValue(message, "salleId");
            String appointmentId = extractValue(message, "id");

            // Send POST request to ressource-service
            Map<String, String> requestBody = Map.of("salleId", salleId, "appointmentId", appointmentId);
            webClient.build()
                    .post()
                    .uri("http://ressource-service/api/ressource/salle/affecter/" + salleId)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .collectList()
                    .block();
        } catch (Exception e) {
            logger.error("Error inserting salle reservation for appointmentId {}: {}", message, e.getMessage(), e);
            throw new BadRequestException("Failed to fetch appointments: " + e.getMessage());
        }
    }

    private String extractValue(String message, String key) {
        String prefix = key + "=";
        int startIndex = message.indexOf(prefix) + prefix.length();
        int endIndex = message.indexOf(",", startIndex);
        if (endIndex == -1) {
            endIndex = message.indexOf(")", startIndex);
        }
        return message.substring(startIndex, endIndex).trim();
    }

}
