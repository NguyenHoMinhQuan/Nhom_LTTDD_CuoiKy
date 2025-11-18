package com.example.server.controller;

import com.example.server.dto.LecturerDTO;
import com.example.server.entity.Lecturer;
import com.example.server.service.LecturerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lecturers")
@CrossOrigin("*")
public class LecturerController {

    private final LecturerService lecturerService;

    public LecturerController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    @GetMapping
    public List<LecturerDTO> getAllLecturers() {
        return lecturerService.getAllLecturers()
                .stream()
                .map(l -> {
                    LecturerDTO dto = new LecturerDTO();
                    dto.setLecturerId(l.getLecturerId());
                    dto.setStaffNumber(l.getStaffNumber());
                    dto.setFullName(l.getFullName());
                    dto.setDepartment(l.getDepartment());
                    return dto;
                })
                .toList();
    }

    @GetMapping("/{id}")
    public LecturerDTO getById(@PathVariable Integer id) {
        Lecturer l = lecturerService.getLecturerById(id);

        LecturerDTO dto = new LecturerDTO();
        dto.setLecturerId(l.getLecturerId());
        dto.setStaffNumber(l.getStaffNumber());
        dto.setFullName(l.getFullName());
        dto.setDepartment(l.getDepartment());

        return dto;
    }
}
