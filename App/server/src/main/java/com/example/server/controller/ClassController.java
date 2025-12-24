package com.example.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.server.dto.ClassDTO;
import com.example.server.service.ClassService;

@RestController
@RequestMapping("/api/classes")
public class ClassController {
    
    @Autowired
    private ClassService classService;

    @GetMapping
    public ResponseEntity<List<ClassDTO>> getAllClasss() {
        List<ClassDTO> classs = classService.findAllClasses();
        return ResponseEntity.ok(classs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassDTO> getClassById(@PathVariable Integer id) {
        return classService.findClassById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/lecturer/{lecturerId}")
    public ResponseEntity<List<ClassDTO>> getClassesByLecturer(@PathVariable Integer lecturerId) {
        List<ClassDTO> classes = classService.findAllByLecturerId(lecturerId);
        return ResponseEntity.ok(classes);
    }
    @PostMapping
    public ResponseEntity<ClassDTO> createClass(@RequestBody ClassDTO classDTO) {
        ClassDTO savedClass = classService.saveClass(classDTO);
        return ResponseEntity.ok(savedClass);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable Integer id) {
        classService.deleteClass(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/lecturer/{lecturerId}")
    public ResponseEntity<List<ClassDTO>> getClassesByLecturer(@PathVariable Integer lecturerId) {
        List<ClassDTO> classes = classService.findAllByLecturerId(lecturerId);
        if (classes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(classes);
    }
}
