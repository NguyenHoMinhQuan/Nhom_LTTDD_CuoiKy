package com.example.server.service;

import com.example.server.dto.ChatMessageDTO;
import com.example.server.entity.ClassMessage;
import com.example.server.repository.MessageRepository;
import com.example.server.repository.UserRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public ChatMessageDTO saveMessage(ChatMessageDTO dto) {
        // 1. Chuyển DTO sang Entity để lưu vào DB
        ClassMessage entity = new ClassMessage();
        entity.setClassId(dto.getClassId());
        entity.setSenderId(dto.getSenderId());
        entity.setContent(dto.getContent());
        // SentAt sẽ được @PrePersist trong Entity tự động điền hoặc gán tại đây:
        entity.setSentAt(LocalDateTime.now());

        ClassMessage savedEntity = messageRepository.save(entity);

        // 2. Cập nhật lại DTO với thông tin đã lưu (thời gian, ID) để gửi trả lại
        dto.setSentAt(savedEntity.getSentAt().format(formatter));
        return dto;
    }

    @Override
    public List<ChatMessageDTO> getMessagesByClassId(Integer classId) {
        return messageRepository.findByClassIdOrderBySentAtAsc(classId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ChatMessageDTO convertToDTO(ClassMessage entity) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setClassId(entity.getClassId());
        dto.setSenderId(entity.getSenderId());
        dto.setContent(entity.getContent());
        dto.setSentAt(entity.getSentAt().format(formatter));
        
        // Lấy tên người gửi từ UserRepository để hiển thị trên App (nếu cần)
        userRepository.findById(entity.getSenderId()).ifPresent(user -> {
            dto.setSenderName(user.getUsername()); // Giả sử User có field FullName
        });
        
        return dto;
    }
}