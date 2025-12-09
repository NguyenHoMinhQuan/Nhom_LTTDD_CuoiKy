package com.example.server.service;

import java.util.List;
import java.util.Optional;

import com.example.server.dto.ClassDTO;

public interface ClassService {
    
    List<ClassDTO> findAllClasss();

    // Phương thức cần thiết để lấy danh sách Lớp theo ID giảng viên
    List<ClassDTO> findAllClassesByLecturerId(Integer lecturerId);

    Optional<ClassDTO> findClassById(Integer id);

    List<ClassDTO> findAllByLecturerId(Integer lecturerId);

    ClassDTO saveClass(ClassDTO classDTO);

    void deleteClass(Integer id);

}
