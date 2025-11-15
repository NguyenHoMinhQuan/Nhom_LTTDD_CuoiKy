package com.example.server.service;

import java.util.List;
import java.util.Optional;

import com.example.server.dto.CourseDTO;

public interface CourseService {
    
    List<CourseDTO> findAllCourseDTOs();

    Optional<CourseDTO> findCourseDTOById(Integer id);

    CourseDTO savCourseDTO(CourseDTO courseDTO);

    void deleteCouseDTO(Integer id);

}
