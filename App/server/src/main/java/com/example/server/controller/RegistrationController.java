package com.example.server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.server.dto.RegistrationDTO;
import com.example.server.service.RegistrationService;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService service;

    public RegistrationController(RegistrationService service) {
        this.service = service;
    }

    @GetMapping
    public List<RegistrationDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public RegistrationDTO getOne(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PostMapping
    public RegistrationDTO create(@RequestBody RegistrationDTO dto) {
        return service.create(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
