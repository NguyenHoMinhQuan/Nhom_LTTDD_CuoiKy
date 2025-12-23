package com.example.server.service;

import com.example.server.dto.LecturerDTO;
import java.util.List;
import java.util.Optional;

public interface LecturerService {

    List<LecturerDTO> findAllLecturers();

    Optional<LecturerDTO> findLecturerById(Integer id);

    LecturerDTO createLecturer(LecturerDTO lecturerDTO);

    Optional<LecturerDTO> updateLecturer(Integer id, LecturerDTO lecturerDTO);

    void deleteLecturer(Integer id);
}
