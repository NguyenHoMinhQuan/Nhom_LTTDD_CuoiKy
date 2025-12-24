package com.example.server.controller;

import com.example.server.dto.ChatMessageDTO;
import com.example.server.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin("*")
public class ChatRestController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/history/{classId}")
    public ResponseEntity<List<ChatMessageDTO>> getChatHistory(
            @PathVariable Integer classId
    ) {
        List<ChatMessageDTO> history =
                messageService.getMessagesByClassId(classId);

        if (history.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(history);
    }
}
