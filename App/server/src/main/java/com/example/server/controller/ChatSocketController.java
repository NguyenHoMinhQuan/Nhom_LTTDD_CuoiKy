package com.example.server.controller;

import com.example.server.dto.ChatMessageDTO;
import com.example.server.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller // ✅ BẮT BUỘC
public class ChatSocketController {

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat/{classId}")
    public void handleChatMessage(
            @DestinationVariable Integer classId,
            ChatMessageDTO message
    ) {
        ChatMessageDTO savedMessage = messageService.saveMessage(message);
        messagingTemplate.convertAndSend(
                "/topic/class/" + classId,
                savedMessage
        );
    }
}
