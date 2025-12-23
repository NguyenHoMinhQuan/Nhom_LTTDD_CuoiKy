package com.example.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.server.dto.ClassScheduleDTO;
import com.example.server.service.ClassScheduleService;

@RestController
@RequestMapping("/api/class-schedules")
public class ClassScheduleController {
    
    @Autowired
    private ClassScheduleService classScheduleService;

    // GET ALL: Lấy tất cả TKB
    @GetMapping
    public ResponseEntity<List<ClassScheduleDTO>> getAllClassSchedules() {
        List<ClassScheduleDTO> classSchedules = classScheduleService.findAllClassSchedules();
        return ResponseEntity.ok(classSchedules);
    }

    // Lấy Thời khóa biểu theo ID Giảng viên
    // Endpoint: GET /api/class-schedules/lecturer/{idgiangvien}
    @GetMapping("/lecturer/{lecturerId}")
    public ResponseEntity<List<ClassScheduleDTO>> getScheduleByLecturerId(
            @PathVariable("lecturerId") Integer lecturerId) {
        
        // 1. Gọi Service để thực hiện logic tổng hợp dữ liệu (joining)
        List<ClassScheduleDTO> schedule = classScheduleService.findScheduleByLecturerId(lecturerId); 
        
        // 2. Trả về kết quả
        if (schedule.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(schedule);
    }

    // GET BY ID: Lấy TKB theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ClassScheduleDTO> getClassScheduleById(@PathVariable Integer id) {
        return classScheduleService.findClassScheduleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: CHỈ DÙNG ĐỂ TẠO MỚI (CREATE)
    @PostMapping
    public ResponseEntity<ClassScheduleDTO> createClassSchedule(@RequestBody ClassScheduleDTO classScheduleDTO) {
        // Cần đảm bảo scheduleId là null khi tạo mới (logic nên nằm trong Service)
        ClassScheduleDTO savedClassSchedule = classScheduleService.saveClassSchedule(classScheduleDTO);
        return new ResponseEntity<>(savedClassSchedule, HttpStatus.CREATED); // Trả về 201 Created
    }

    // PUT: CHỈ DÙNG ĐỂ CẬP NHẬT (UPDATE)
    @PutMapping("/{id}")
    public ResponseEntity<ClassScheduleDTO> updateClassSchedule(
            @PathVariable Integer id,
            @RequestBody ClassScheduleDTO classScheduleDTO) {
        
        // Đảm bảo ID trong path và ID trong body khớp nhau
        if (classScheduleDTO.getScheduleId() == null || !classScheduleDTO.getScheduleId().equals(id)) {
            // Có thể thêm logic kiểm tra ID không hợp lệ (400 Bad Request)
            return ResponseEntity.badRequest().build(); 
        }

        ClassScheduleDTO updatedClassSchedule = classScheduleService.saveClassSchedule(classScheduleDTO);
        return ResponseEntity.ok(updatedClassSchedule); // Trả về 200 OK
    }

    // DELETE: Xóa theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassSchedule(@PathVariable Integer id) {
        classScheduleService.deleteClassSchedule(id);
        return ResponseEntity.noContent().build();
    }
}