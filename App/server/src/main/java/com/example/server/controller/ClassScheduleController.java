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

    @GetMapping
    public ResponseEntity<List<ClassScheduleDTO>> getAllClassSchedules() {
        List<ClassScheduleDTO> classSchedules = classScheduleService.findAllClassSchedules();
        return ResponseEntity.ok(classSchedules);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassScheduleDTO> getClassScheduleById(@PathVariable Integer id) {
        return classScheduleService.findClassScheduleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClassScheduleDTO> createClassSchedule(@RequestBody ClassScheduleDTO classScheduleDTO) {
        ClassScheduleDTO savedClassSchedule = classScheduleService.savClassScheduleDTO(classScheduleDTO);
        if(classScheduleDTO.getScheduleId() != null) {
            return ResponseEntity.ok(savedClassSchedule);
        } else {
            return new ResponseEntity<>(savedClassSchedule, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassSchedule(@PathVariable Integer id) {
        classScheduleService.deleteClassScheduleDTO(id);
        return ResponseEntity.noContent().build();
    }

}
