package com.example.server.controller;

import com.example.server.dto.HocVien_TinNhanDto;
import com.example.server.service.HocVien_TinNhanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/chat")
public class HocVien_TinNhanController {

    @Autowired
    private HocVien_TinNhanService tinNhanService;

    // API: GET /api/student/chat?username=student1&classCode=CT101_B
    @GetMapping
    public ResponseEntity<List<HocVien_TinNhanDto>> getMessages(
            @RequestParam String username,
            @RequestParam String classCode) {

        List<HocVien_TinNhanDto> list = tinNhanService.getMessagesByClass(username, classCode);
        return ResponseEntity.ok(list);
    }
}