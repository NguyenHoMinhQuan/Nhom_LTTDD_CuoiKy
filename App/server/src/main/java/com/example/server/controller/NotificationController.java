package com.example.server.controller;

import com.example.server.dto.NotificationDTO;
import com.example.server.entity.Notification;
import com.example.server.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin("*")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<NotificationDTO> getAllNotifications() {
        return notificationService.getAllNotifications()
                .stream()
                .map(n -> {
                    NotificationDTO dto = new NotificationDTO();
                    dto.setNotificationId(n.getNotificationId());
                    dto.setUserId(n.getUserId());
                    dto.setAnnouncementId(n.getAnnouncementId());
                    dto.setIsRead(n.getIsRead());
                    dto.setCreatedAt(n.getCreatedAt());
                    return dto;
                })
                .toList();
    }

    @GetMapping("/{id}")
    public NotificationDTO getById(@PathVariable Integer id) {
        Notification n = notificationService.getNotificationById(id);

        NotificationDTO dto = new NotificationDTO();
        dto.setNotificationId(n.getNotificationId());
        dto.setUserId(n.getUserId());
        dto.setAnnouncementId(n.getAnnouncementId());
        dto.setIsRead(n.getIsRead());
        dto.setCreatedAt(n.getCreatedAt());

        return dto;
    }
}
