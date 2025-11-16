package com.example.server.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.server.dto.RegistrationDTO;
import com.example.server.entity.Registration;
import com.example.server.repository.RegistrationRepository;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository repo;

    public RegistrationServiceImpl(RegistrationRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<RegistrationDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public RegistrationDTO getById(Integer id) {
        return repo.findById(id).map(this::toDTO).orElse(null);
    }

    @Override
    public RegistrationDTO create(RegistrationDTO dto) {
        Registration r = toEntity(dto);
        r = repo.save(r);
        return toDTO(r);
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }

    private RegistrationDTO toDTO(Registration r) {
        RegistrationDTO dto = new RegistrationDTO();
        dto.setRegistrationId(r.getRegistrationId());
        dto.setStudentId(r.getStudentId());
        dto.setClassId(r.getClassId());
        dto.setRegisteredAt(r.getRegisteredAt());
        dto.setStatus(r.getStatus());
        return dto;
    }

    private Registration toEntity(RegistrationDTO dto) {
        Registration r = new Registration();
        r.setRegistrationId(dto.getRegistrationId());
        r.setStudentId(dto.getStudentId());
        r.setClassId(dto.getClassId());
        r.setRegisteredAt(dto.getRegisteredAt());
        r.setStatus(dto.getStatus());
        return r;
    }
}
