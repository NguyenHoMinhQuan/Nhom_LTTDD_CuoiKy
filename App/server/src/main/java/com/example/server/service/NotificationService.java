package com.example.server.service;

import com.example.server.dto.NotificationDTO;
import com.example.server.entity.Notification;

import java.util.List;

public interface NotificationService {

    List<Notification> getAllNotifications();

    Notification getNotificationById(Integer id);

    Notification updateNotification(Integer id, NotificationDTO dto);

    void markAsRead(Integer id);
}
