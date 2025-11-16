package com.example.server.service;

import com.example.server.entity.Notification;
import com.example.server.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification getNotificationById(Integer id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Notification ID: " + id));
    }
}
