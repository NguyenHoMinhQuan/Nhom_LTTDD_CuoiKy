package com.example.server.service;

import com.example.server.dto.LecturerDTO;
import com.example.server.entity.Lecturer;
import com.example.server.repository.LecturerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LecturerServiceImpl implements LecturerService {

    private final LecturerRepository lecturerRepository;

    public LecturerServiceImpl(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    // ===== convert =====
    private LecturerDTO convertToDTO(Lecturer l) {
        LecturerDTO dto = new LecturerDTO();
        dto.setLecturerId(l.getLecturerId());
        dto.setStaffNumber(l.getStaffNumber());
        dto.setFullName(l.getFullName());
        dto.setDepartment(l.getDepartment());
        return dto;
    }

    private Lecturer convertToEntity(LecturerDTO dto) {
        Lecturer l = new Lecturer();
        l.setLecturerId(dto.getLecturerId());
        l.setStaffNumber(dto.getStaffNumber());
        l.setFullName(dto.getFullName());
        l.setDepartment(dto.getDepartment());
        return l;
    }

    // ===== CRUD =====
    @Override
    public List<LecturerDTO> findAllLecturers() {
        return lecturerRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public Optional<LecturerDTO> findLecturerById(Integer id) {
        return lecturerRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public LecturerDTO createLecturer(LecturerDTO lecturerDTO) {
        Lecturer lecturer = convertToEntity(lecturerDTO);
        Lecturer saved = lecturerRepository.save(lecturer);
        return convertToDTO(saved);
    }

    @Override
    public Optional<LecturerDTO> updateLecturer(Integer id, LecturerDTO lecturerDTO) {
        return lecturerRepository.findById(id)
                .map(existing -> {
                    existing.setStaffNumber(lecturerDTO.getStaffNumber());
                    existing.setFullName(lecturerDTO.getFullName());
                    existing.setDepartment(lecturerDTO.getDepartment());
                    return convertToDTO(lecturerRepository.save(existing));
                });
    }

    @Override
    public void deleteLecturer(Integer id) {
        lecturerRepository.deleteById(id);
    }
}
