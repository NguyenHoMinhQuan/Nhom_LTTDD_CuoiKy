package com.example.server.controller;

import com.example.server.dto.DanhGiaDto; // Nhớ import DTO
import com.example.server.service.DanhGiaService; // Nhớ import Service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
public class DanhGiaController {

    @Autowired
    private DanhGiaService danhGiaService; // Gọi Service chứ không gọi Repo nữa

    @PostMapping("/gui")
    public ResponseEntity<?> guiDanhGia(@RequestBody DanhGiaDto dto) {
        try {
            // Gọi Service để xử lý lưu
            danhGiaService.luuDanhGia(dto);
            return ResponseEntity.ok("Gửi đánh giá thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }
}