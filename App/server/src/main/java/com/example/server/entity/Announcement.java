package com.example.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "[Announcement]")
public class Announcement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AnnouncementId")
    private Integer AnnouncementId;

    @Column(name = "Title")
    private String Title;

    @Column(name = "Body")
    private String Body;

    @Column(name = "AuthorId")
    private String AuthorId;

    @Column(name = "IsGlobal")
    private Boolean IsGlobal;

    @Column(name = "TargetClassId")
    private String TargetClassId;

    @Column(name = "CreatedAt")
    private String CreatedAt;

    @Column(name = "UpdatedAt")
    private String UpdatedAt;

    public Announcement() {
    }

    public Integer getAnnouncementId() {
        return AnnouncementId;
    }

    public void setAnnouncementId(Integer announcementId) {
        this.AnnouncementId = announcementId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        this.Body = body;
    }

    public String getAuthorId() {
        return AuthorId;
    }

    public void setAuthorId(String authorId) {
        this.AuthorId = authorId;
    }

    public Boolean getIsGlobal() {
        return IsGlobal;
    }

    public void setIsGlobal(Boolean isGlobal) {
        this.IsGlobal = isGlobal;
    }

    public String getTargetClassId() {
        return TargetClassId;
    }

    public void setTargetClassId(String targetClassId) {
        this.TargetClassId = targetClassId;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }   

    public void setCreatedAt(String createdAt) {
        this.CreatedAt = createdAt;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.UpdatedAt = updatedAt;
    }

}
