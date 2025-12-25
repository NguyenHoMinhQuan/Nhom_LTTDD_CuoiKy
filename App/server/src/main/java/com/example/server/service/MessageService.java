package com.example.server.service;

import java.util.List;

import com.example.server.dto.ChatMessageDTO;

public interface MessageService {
    // Lưu tin nhắn mới vào database và trả về DTO để phát qua WebSocket
    ChatMessageDTO saveMessage(ChatMessageDTO chatMessageDTO);

    // Lấy lịch sử tin nhắn của một lớp học
    List<ChatMessageDTO> getMessagesByClassId(Integer classId);
}
