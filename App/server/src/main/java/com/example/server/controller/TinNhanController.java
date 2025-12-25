package com.example.server.controller;

import com.example.server.entity.TinNhan;
import com.example.server.repository.TinNhanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/chat")
public class TinNhanController {
    @Autowired private TinNhanRepository repo;

    @GetMapping("/lop/{idLop}")
    public ResponseEntity<?> layTinNhan(@PathVariable Integer idLop) {
        return ResponseEntity.ok(repo.findByClassIdOrderByThoiGianGuiAsc(idLop));
    }

    @PostMapping("/gui")
    public ResponseEntity<?> guiTinNhan(@RequestBody TinNhan tinNhan) {
        tinNhan.setThoiGianGui(LocalDateTime.now());
        return ResponseEntity.ok(repo.save(tinNhan));
    }
}