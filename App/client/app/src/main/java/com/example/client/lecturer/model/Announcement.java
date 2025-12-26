package com.example.client.lecturer.model;

import java.io.Serializable;

public class Announcement implements Serializable {

    private Integer announcementId; // Khớp với DB
    private String title;
    private String body;
    private String authorId;
    private Boolean isGlobal;
    private String targetClassId;
    private String createdAt;
    private String updatedAt;

    // Constructor không tham số (Bắt buộc cho Gson/Retrofit)
    public Announcement() {
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

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Boolean getIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(Boolean global) {
        isGlobal = global;
    }

    public String getTargetClassId() {
        return targetClassId;
    }

    public void setTargetClassId(String targetClassId) {
        this.targetClassId = targetClassId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}