package com.example.server.dto;

public class AnnouncementDTO {
    
    private Integer announcementId;
    private String title;
    private String body;
    private String authorId;
    private Boolean isGlobal;
    private String targetClassId;
    private String createdAt;
    private String updatedAt;

    public AnnouncementDTO() {
    }

    public AnnouncementDTO(Integer announcementId, String title, String body, String authorId, Boolean isGlobal, String targetClassId, String createdAt, String updatedAt) {
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

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Boolean getIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(Boolean isGlobal) {
        this.isGlobal = isGlobal;
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
