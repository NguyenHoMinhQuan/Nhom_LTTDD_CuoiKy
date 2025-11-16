package com.example.server.service;

import java.util.List;
import com.example.server.dto.RegistrationDTO;

public interface RegistrationService {
    List<RegistrationDTO> getAll();

    RegistrationDTO getById(Integer id);

    RegistrationDTO create(RegistrationDTO dto);

    void delete(Integer id);
}
