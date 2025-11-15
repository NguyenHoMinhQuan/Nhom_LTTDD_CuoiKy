package com.example.server.service;

import java.util.List;
import java.util.Optional;

import com.example.server.dto.ClassDTO;

public interface ClassService {
    
    List<ClassDTO> findAllClasss();

    Optional<ClassDTO> findClassById(Integer id);

    ClassDTO saveClass(ClassDTO classDTO);

    void deleteClass(Integer id);

}
