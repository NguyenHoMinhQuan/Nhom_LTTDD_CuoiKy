package com.example.server.service;

import com.example.server.dto.StudentDTO;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    List<StudentDTO> findAllStudents();

    Optional<StudentDTO> findStudentById(Integer id);

    StudentDTO createStudent(StudentDTO studentDTO);

    Optional<StudentDTO> updateStudent(Integer id, StudentDTO studentDTO);

    void deleteStudent(Integer id);
}
