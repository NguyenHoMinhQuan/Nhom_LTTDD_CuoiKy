package com.example.server.service;

import java.util.List;
import java.util.Optional;

import com.example.server.dto.ClassScheduleDTO;

public interface ClassScheduleService {
    
    List<ClassScheduleDTO> findScheduleByLecturerIdForToday(Integer lecturerId);

    List<ClassScheduleDTO> findAllClassSchedules();

    List<ClassScheduleDTO> findScheduleByLecturerId(Integer lecturerId);

    Optional<ClassScheduleDTO> findClassScheduleById(Integer id);

    List<ClassScheduleDTO> findAllByClassId(Integer classId);

    ClassScheduleDTO saveClassSchedule(ClassScheduleDTO classScheduleDTO);

    void deleteClassSchedule(Integer id);

}
