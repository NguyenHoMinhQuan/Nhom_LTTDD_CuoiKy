package com.example.server.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.dto.ClassScheduleDTO;
import com.example.server.entity.ClassSchedule;
import com.example.server.repository.ClassScheduleRepository;

@Service
public class ClassScheduleServiceImpl implements ClassScheduleService {
 
    @Autowired
    private ClassScheduleRepository classScheduleRepository;

    public ClassScheduleDTO convertToDTO(ClassSchedule classSchedule) {
        ClassScheduleDTO classScheduleDTO = new ClassScheduleDTO();
        classScheduleDTO.setScheduleId(classSchedule.getScheduleId());
        classScheduleDTO.setClassId(classSchedule.getClassId());
        classScheduleDTO.setDayOfWeek(classSchedule.getDayOfWeek());
        classScheduleDTO.setStartTime(classSchedule.getStartTime());
        classScheduleDTO.setEndTime(classSchedule.getEndTime());
        classScheduleDTO.setRoom(classSchedule.getRoom());
        return classScheduleDTO;
    }

    public ClassSchedule convertToEntity(ClassScheduleDTO classScheduleDTO) {
        ClassSchedule classSchedule = new ClassSchedule();
        classSchedule.setScheduleId(classScheduleDTO.getScheduleId());
        classSchedule.setClassId(classScheduleDTO.getClassId());
        classSchedule.setDayOfWeek(classScheduleDTO.getDayOfWeek());
        classSchedule.setStartTime(classScheduleDTO.getStartTime());
        classSchedule.setEndTime(classScheduleDTO.getEndTime());
        classSchedule.setRoom(classScheduleDTO.getRoom());
        return classSchedule;
    }

    @Override
    public List<ClassScheduleDTO> findAllClassSchedules() {
        List<ClassSchedule> classSchedules = classScheduleRepository.findAll();
        return classSchedules.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ClassScheduleDTO> findClassScheduleById(Integer id) {
        return classScheduleRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public ClassScheduleDTO savClassScheduleDTO(ClassScheduleDTO classScheduleDTO) {
        ClassSchedule classScheduleToSave = convertToEntity(classScheduleDTO);
        ClassSchedule savedClassSchedule = classScheduleRepository.save(classScheduleToSave);
        return convertToDTO(savedClassSchedule);
    }

    @Override
    public void deleteClassScheduleDTO(Integer id) {
        classScheduleRepository.deleteById(id);
    }

}
