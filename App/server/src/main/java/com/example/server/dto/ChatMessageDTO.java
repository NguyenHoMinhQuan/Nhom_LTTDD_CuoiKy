package com.example.server.dto;

public class ChatMessageDTO {
    private Integer classId;
    private Integer senderId;
    private String senderName;
    private String content;
    private String sentAt;
    public Integer getClassId() {
        return classId;
    }
    public void setClassId(Integer classId) {
        this.classId = classId;
    }
    public Integer getSenderId() {
        return senderId;
    }
    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }
    public String getSenderName() {
        return senderName;
    }
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getSentAt() {
        return sentAt;
    }
    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }
}