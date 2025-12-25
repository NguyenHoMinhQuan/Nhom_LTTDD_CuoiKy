package com.example.server.service;

import java.util.List;
import java.util.Optional;

import com.example.server.dto.AssignmentDTO;

public interface AssignmentService {
    List<AssignmentDTO> findAllAssignments();
    List<AssignmentDTO> findAssignmentsByClassId(Integer classId);
    Optional<AssignmentDTO> findAssignmentById(Integer id);
    AssignmentDTO saveAssignment(AssignmentDTO assignmentDTO);
    void deleteAssignment(Integer id);
    List<AssignmentDTO> findAssignmentsByLecturerId(Integer lecturerId);
}
