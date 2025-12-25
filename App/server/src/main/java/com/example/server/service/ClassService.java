package com.example.server.service;

import java.util.List;
import java.util.Optional;

import com.example.server.dto.ClassDTO;

public interface ClassService {
    
    List<ClassDTO> findAllClasses();

    Optional<ClassDTO> findClassById(Integer id);

    List<ClassDTO> findAllByLecturerId(Integer lecturerId);

    ClassDTO saveClass(ClassDTO classDTO);

    void deleteClass(Integer id);

}
