package com.example.server.service;

import com.example.server.dto.StudentDTO;
import com.example.server.entity.Student;
import com.example.server.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // 🔹 GET ALL
    @Override
    public List<StudentDTO> findAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // 🔹 GET BY ID
    @Override
    public Optional<StudentDTO> findStudentById(Integer id) {
        return studentRepository.findById(id)
                .map(this::toDTO);
    }

    // 🔹 CREATE
    @Override
    public StudentDTO createStudent(StudentDTO dto) {
        Student student = new Student();
        student.setStudentNumber(dto.getStudentNumber());
        student.setFullName(dto.getFullName());
        student.setFaculty(dto.getFaculty());
        student.setYear(dto.getYear());

        return toDTO(studentRepository.save(student));
    }

    // 🔹 UPDATE
    @Override
    public Optional<StudentDTO> updateStudent(Integer id, StudentDTO dto) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setStudentNumber(dto.getStudentNumber());
                    student.setFullName(dto.getFullName());
                    student.setFaculty(dto.getFaculty());
                    student.setYear(dto.getYear());
                    return toDTO(studentRepository.save(student));
                });
    }

    // 🔹 DELETE
    @Override
    public void deleteStudent(Integer id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy sinh viên ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    // 🔁 Mapper
    private StudentDTO toDTO(Student s) {
        return new StudentDTO(
                s.getStudentId(),
                s.getStudentNumber(),
                s.getFullName(),
                s.getFaculty(),
                s.getYear());
    }
}
