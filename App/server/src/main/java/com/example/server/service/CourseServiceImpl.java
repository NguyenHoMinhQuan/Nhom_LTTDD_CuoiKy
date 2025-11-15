package com.example.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.dto.CourseDTO;
import com.example.server.entity.Course;
import com.example.server.repository.CourseRepository;

@Service
public class CourseServiceImpl implements CourseService {
    
    @Autowired
    private CourseRepository courseRepository;

    public CourseDTO convertToDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseId(course.getCourseId());
        courseDTO.setCourseCode(course.getCourseCode());
        courseDTO.setCourseName(course.getCourseName());
        courseDTO.setCredit(course.getCredit());
        courseDTO.setDescription(course.getDescription());
        return courseDTO;
    }

    public Course convertToEntity(CourseDTO courseDTO) {
        Course course = new Course();
        course.setCourseId(courseDTO.getCourseId());
        course.setCourseCode(courseDTO.getCourseCode());
        course.setCourseName(courseDTO.getCourseName());
        course.setCredit(courseDTO.getCredit());
        course.setDescription(courseDTO.getDescription());
        return course;
    }

    @Override
    public List<CourseDTO> findAllCourseDTOs() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream().map(this::convertToDTO).toList();
    }

    @Override
    public Optional<CourseDTO> findCourseDTOById(Integer id) {
        return courseRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public CourseDTO savCourseDTO(CourseDTO courseDTO) {
        Course course = convertToEntity(courseDTO);
        course = courseRepository.save(course);
        return convertToDTO(course);
    }

    @Override
    public void deleteCouseDTO(Integer id) {
        courseRepository.deleteById(id);
    }
}
