package com.example.server.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.dto.ClassDTO;
import com.example.server.repository.ClassRepository;
import com.example.server.entity.Class;

@Service
public class ClassServiceImpl implements ClassService{
    
    @Autowired
    private ClassRepository classRepository;

    private ClassDTO convertToDTO(Class _class) {
        ClassDTO classDTO = new ClassDTO();
        classDTO.setClassId(_class.getClassId());
        classDTO.setCapacity(_class.getCapacity());
        classDTO.setClassCode(_class.getClassCode());
        classDTO.setCourseId(_class.getCourseId());
        classDTO.setCreatedAt(_class.getCreatedAt());
        classDTO.setCreatedBy(_class.getCreatedBy());
        classDTO.setLecturerId(_class.getLecturerId());
        classDTO.setSemester(_class.getSemester());

        return classDTO;
    }

    private Class convertToEntity(ClassDTO classDTO) {
        // Ánh xạ từ DTO sang Entity
        Class _class = new Class();
        _class.setClassId(classDTO.getClassId());
        _class.setCapacity(classDTO.getCapacity());
        _class.setClassCode(classDTO.getClassCode());
        _class.setCourseId(classDTO.getCourseId());
        _class.setCreatedAt(classDTO.getCreatedAt());
        _class.setCreatedBy(classDTO.getCreatedBy());
        _class.setLecturerId(classDTO.getLecturerId());
        _class.setSemester(classDTO.getSemester());
        return _class;
    }

    @Override
    public List<ClassDTO> findAllClasses() {
        List<Class> classes = classRepository.findAll();
        return classes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    };

    @Override
    public Optional<ClassDTO> findClassById(Integer id) {
        return classRepository.findById(id)
                .map(this::convertToDTO);
    };

    @Override
    public List<ClassDTO> findAllByLecturerId(Integer lecturerId) {
        return classRepository.findAllByLecturerId(lecturerId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public ClassDTO saveClass(ClassDTO classDTO){
        Class classToSave = convertToEntity(classDTO);
        Class savedClass = classRepository.save(classToSave);
        return convertToDTO(savedClass);
    };


    @Override
    public void deleteClass(Integer id) {
        classRepository.deleteById(id);
    };

}
