package com.example.server.handler;

import com.example.server.dto.ChatMessageDTO;
import com.example.server.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final MessageService messageService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    // Quản lý danh sách các máy đang kết nối
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    public ChatWebSocketHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session); // Khi có người vào app thì lưu session lại
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ChatMessageDTO dto = objectMapper.readValue(payload, ChatMessageDTO.class);

        // Lưu vào DB (Lưu ý: dto phải có classId từ Android gửi lên)
        ChatMessageDTO savedMsg = messageService.saveMessage(dto);

        // Phát tin nhắn (Broadcast)
        String jsonResponse = objectMapper.writeValueAsString(savedMsg);
        synchronized (sessions) {
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(jsonResponse));
                }
            }
        }
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        sessions.remove(session); // Thoát app thì xóa session
    }
}