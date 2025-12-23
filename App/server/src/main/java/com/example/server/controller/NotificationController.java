package com.example.server.controller;

import com.example.server.dto.NotificationDTO;
import com.example.server.entity.Notification;
import com.example.server.service.NotificationService;
import org.springframework.http.ResponseEntity;
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

    // 🔹 GET ALL
    @GetMapping
    public List<NotificationDTO> getAllNotifications() {
        return notificationService.getAllNotifications()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // 🔹 GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getById(@PathVariable Integer id) {
        Notification n = notificationService.getNotificationById(id);
        return ResponseEntity.ok(convertToDTO(n));
    }

    // 🔹 UPDATE theo ID
    @PutMapping("/{id}")
    public ResponseEntity<NotificationDTO> updateNotification(
            @PathVariable Integer id,
            @RequestBody NotificationDTO dto) {

        Notification updated = notificationService.updateNotification(id, dto);
        return ResponseEntity.ok(convertToDTO(updated));
    }

    // 🔹 MARK AS READ (thực tế rất hay dùng)
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Integer id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    // ===== Mapper =====
    private NotificationDTO convertToDTO(Notification n) {
        NotificationDTO dto = new NotificationDTO();
        dto.setNotificationId(n.getNotificationId());
        dto.setUserId(n.getUserId());
        dto.setAnnouncementId(n.getAnnouncementId());
        dto.setIsRead(n.getIsRead());
        dto.setCreatedAt(n.getCreatedAt());
        return dto;
    }
}
