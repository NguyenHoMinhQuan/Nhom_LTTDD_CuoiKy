package com.example.server.service;

import java.util.List;
import java.util.Optional;

import com.example.server.dto.ClassScheduleDTO;

public interface ClassScheduleService {
    
    List<ClassScheduleDTO> findAllClassSchedules();

    Optional<ClassScheduleDTO> findClassScheduleById(Integer id);

    ClassScheduleDTO savClassScheduleDTO(ClassScheduleDTO classScheduleDTO);

    void deleteClassScheduleDTO(Integer id);

}
