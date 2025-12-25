package com.example.server.dto;

import java.time.LocalDateTime;

public class AnnouncementDTO {
    
    private Integer announcementId;
    private String title;
    private String body;
    private Integer authorId;
    private Boolean isGlobal;
    private Integer targetClassId;
    private LocalDateTime createdAt;
    private String updatedAt;

    public AnnouncementDTO() {
    }

    public AnnouncementDTO(Integer announcementId, String title, String body, Integer authorId, Boolean isGlobal, Integer targetClassId, LocalDateTime createdAt, String updatedAt) {
        this.announcementId = announcementId;
        this.title = title;
        this.body = body;
        this.authorId = authorId;
        this.isGlobal = isGlobal;
        this.targetClassId = targetClassId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(Integer announcementId) {
        this.announcementId = announcementId;
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

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Boolean getIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(Boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public Integer getTargetClassId() {
        return targetClassId;
    }

    public void setTargetClassId(Integer targetClassId) {
        this.targetClassId = targetClassId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
