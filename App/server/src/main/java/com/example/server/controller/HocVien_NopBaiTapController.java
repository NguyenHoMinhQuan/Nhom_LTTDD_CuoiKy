package com.example.server.controller;

import com.example.server.dto.HocVien_NopBaiTap;
import com.example.server.service.AssignmentService;
import com.example.server.service.HocVien_NopBaiTapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/assignment")
public class HocVien_NopBaiTapController {

    private final HocVien_NopBaiTapService assignmentService;

    public HocVien_NopBaiTapController(HocVien_NopBaiTapService assignmentService) {
        this.assignmentService = assignmentService;
    }

    // API nộp bài
    // URL: http://localhost:8080/api/assignment/submit
    // Method: POST
    @PostMapping("/submit")
    public ResponseEntity<?> submitAssignment(@RequestBody HocVien_NopBaiTap baiTap) {
        boolean isSuccess = assignmentService.nopBaiTap(baiTap);

        Map<String, Object> response = new HashMap<>();
        if (isSuccess) {
            response.put("success", true);
            response.put("message", "Nộp bài thành công!");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Nộp bài thất bại. Vui lòng thử lại!");
            return ResponseEntity.badRequest().body(response);
        }
    }
}