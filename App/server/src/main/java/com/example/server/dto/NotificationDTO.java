package com.example.server.dto;

import java.time.LocalDateTime;

public class NotificationDTO {

    private Integer notificationId;
    private String title;      // Lấy từ Announcement
    private String body;       // Lấy từ Announcement
    private LocalDateTime createdAt;  // Thời gian tạo
    private Integer isRead;

    public NotificationDTO(Integer notificationId, String title, String body, LocalDateTime createdAt, Integer isRead) {
        this.notificationId = notificationId;
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    public NotificationDTO() {}

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getIsRead() {
        return isRead;
    }    

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }
}
