package com.example.appointmentservice.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    private String message;
    private LocalDateTime timestamp;
    private String service;
}
