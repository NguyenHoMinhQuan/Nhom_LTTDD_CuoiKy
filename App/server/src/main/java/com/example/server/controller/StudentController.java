package com.example.server.controller;

import com.example.server.dto.StudentDTO;
import com.example.server.entity.Student;
import com.example.server.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<StudentDTO> getAllStudents() {
        return studentService.getAllStudents().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{studentNumber}")
    public StudentDTO getStudentByNumber(@PathVariable String studentNumber) {
        Student student = studentService.getStudentByNumber(studentNumber);
        return convertToDTO(student);
    }

    private StudentDTO convertToDTO(Student student) {
        if (student == null)
            return null;
        return new StudentDTO(
                student.getStudentId(),
                student.getStudentNumber(),
                student.getFullName(),
                student.getFaculty(),
                student.getYear());
    }
}
