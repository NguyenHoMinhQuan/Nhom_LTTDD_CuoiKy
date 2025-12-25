package com.example.server.service;

import java.util.List;
import java.util.Optional;

import com.example.server.dto.CourseDTO;

public interface CourseService {
    
    List<CourseDTO> findAllCourses();

    Optional<CourseDTO> findCourseById(Integer id);

    CourseDTO saveCourse(CourseDTO courseDTO);
        
    void deleteCourse(Integer id);

}
