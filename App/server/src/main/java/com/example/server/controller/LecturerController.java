package com.example.server.controller;

import com.example.server.dto.LecturerDTO;
import com.example.server.service.LecturerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lecturers")
@CrossOrigin("*")
public class LecturerController {

    private final LecturerService lecturerService;

    public LecturerController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }


    @GetMapping
    public ResponseEntity<List<LecturerDTO>> getAllLecturers() {
        return ResponseEntity.ok(lecturerService.findAllLecturers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LecturerDTO> getLecturerById(@PathVariable Integer id) {
        return lecturerService.findLecturerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<LecturerDTO> createLecturer(
            @RequestBody LecturerDTO lecturerDTO) {

        LecturerDTO saved = lecturerService.createLecturer(lecturerDTO);
        return ResponseEntity.status(201).body(saved);
    }


    @PutMapping("/{id}")
    public ResponseEntity<LecturerDTO> updateLecturer(
            @PathVariable Integer id,
            @RequestBody LecturerDTO lecturerDTO) {

        return lecturerService.updateLecturer(id, lecturerDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLecturer(@PathVariable Integer id) {
        lecturerService.deleteLecturer(id);
        return ResponseEntity.noContent().build();
    }
}
