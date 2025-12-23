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

    // 🔹 Lấy thông báo chưa đọc cho Dashboard
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(@PathVariable Integer userId) {
        // SỬA LỖI: Gọi đúng tên hàm trong Service của bạn
        List<NotificationDTO> list = notificationService.getUnreadNotificationsByUserId(userId);
        return ResponseEntity.ok(list);
    }
    
    // Thêm endpoint đánh dấu đã đọc khi người dùng click
    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Integer id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    // 🔹 Lấy TOÀN BỘ thông báo của một User (cả cũ và mới)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getAllNotificationsByUserId(@PathVariable Integer userId) {
        List<NotificationDTO> list = notificationService.getAllNotificationsByUserId(userId);
        return ResponseEntity.ok(list);
    }

    // ===== Mapper =====
    private NotificationDTO convertToDTO(Notification n) {
        NotificationDTO dto = new NotificationDTO();
        
        // 1. Chỉ set những trường THỰC SỰ CÓ trong NotificationDTO.java của bạn
        dto.setNotificationId(n.getNotificationId());
        dto.setIsRead(n.getIsRead());
        dto.setCreatedAt(n.getCreatedAt());
        
        // 2. Lấy nội dung từ bảng Announcement liên kết
        // Dùng kiểm tra null để tránh NullPointerException
        if (n.getAnnouncement() != null) {
            dto.setTitle(n.getAnnouncement().getTitle()); 
            dto.setBody(n.getAnnouncement().getBody());
        }
        
        return dto;
    }
}
