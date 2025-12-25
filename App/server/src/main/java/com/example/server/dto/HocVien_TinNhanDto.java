package com.example.server.dto;

import java.util.Date;

// Sử dụng Interface để hứng dữ liệu từ Native Query nhanh nhất
public interface HocVien_TinNhanDto {

    Integer getMessageId();

    Integer getClassId();

    String getClassCode();

    Integer getSenderId();

    String getSenderUsername();

    String getSenderName(); // Map với cột alias "SenderName" trong SQL

    String getContent();

    Date getSentAt(); // Có thể dùng String nếu muốn server trả về text ngày tháng
}