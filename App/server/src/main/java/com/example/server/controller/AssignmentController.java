package com.example.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.dto.AssignmentDTO;
import com.example.server.service.AssignmentService;


@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {
    @Autowired
    private AssignmentService assignmentService;
    @GetMapping
    public ResponseEntity<List<AssignmentDTO>> getAllAssignments() {
        List<AssignmentDTO> assignments = assignmentService.findAllAssignments();
        return ResponseEntity.ok(assignments);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AssignmentDTO> getAssignmentById(@PathVariable Integer id) {
        return assignmentService.findAssignmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/byClass/{classId}")
    public ResponseEntity<List<AssignmentDTO>> getAssignmentsByClassId(@PathVariable Integer classId) {
        List<AssignmentDTO> assignments = assignmentService.findAssignmentsByClassId(classId);
        return ResponseEntity.ok(assignments);
    
    }
    
    @PostMapping
    public ResponseEntity<AssignmentDTO> createOrUpdateAssignment(@RequestBody AssignmentDTO assignmentDTO) {
        AssignmentDTO savedAssignment = assignmentService.saveAssignment(assignmentDTO);
        if (assignmentDTO.getAssignmentId() == null) {
            return ResponseEntity.status(201).body(savedAssignment);
        }
        return ResponseEntity.ok(savedAssignment);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Integer id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/lecturer/{lecturerId}")
    public ResponseEntity<List<AssignmentDTO>> getAssignmentsByLecturer(@PathVariable Integer lecturerId) {
        List<AssignmentDTO> assignments = assignmentService.findAssignmentsByLecturerId(lecturerId);
        return ResponseEntity.ok(assignments);
    }

    
}
