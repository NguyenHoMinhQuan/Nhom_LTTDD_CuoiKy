package com.example.server.dto;
import java.time.LocalDateTime;

public interface TinNhanDto {
    Integer getMessageId();
    Integer getClassId();
    Integer getSenderId();
    String getSenderName(); // Tên người gửi (GV hoặc SV)
    String getContent();
    LocalDateTime getSentAt();
}