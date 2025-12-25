package com.example.server.controller;

import com.example.server.entity.DanhGia;
import com.example.server.repository.DanhGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/feedback")
public class DanhGiaController {
    @Autowired private DanhGiaRepository repo;

    @PostMapping("/gui")
    public ResponseEntity<?> guiDanhGia(@RequestBody DanhGia danhGia) {
        danhGia.setNgayTao(LocalDateTime.now());
        repo.save(danhGia);
        return ResponseEntity.ok("Thành công");
    }
}