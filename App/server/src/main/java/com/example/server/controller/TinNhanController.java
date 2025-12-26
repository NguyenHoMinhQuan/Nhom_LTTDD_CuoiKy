package com.example.server.controller;

import com.example.server.entity.TinNhan;
import com.example.server.service.TinNhanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class TinNhanController {

    @Autowired
    private TinNhanService tinNhanService;

    // API lấy lịch sử: Dùng ID lớp (Integer) cho chuẩn
    @GetMapping("/lop/{idLop}")
    public ResponseEntity<?> layTinNhan(@PathVariable Integer idLop) {
        return ResponseEntity.ok(tinNhanService.layLichSuChat(idLop));
    }

    @PostMapping("/gui")
    public ResponseEntity<?> guiTinNhan(@RequestBody TinNhan tinNhan) {
        try {
            return ResponseEntity.ok(tinNhanService.guiTinNhan(tinNhan));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}