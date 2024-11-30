package com.example.appointmentservice.service;

import com.example.appointmentservice.model.Notification;

public interface NotificationService {
    void publishNotification(Notification notification);
}
