package com.example.server.service;

import com.example.server.entity.Lecturer;
import com.example.server.repository.LecturerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LecturerServiceImpl implements LecturerService {

    private final LecturerRepository lecturerRepository;

    public LecturerServiceImpl(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    @Override
    public List<Lecturer> getAllLecturers() {
        return lecturerRepository.findAll();
    }

    @Override
    public Lecturer getLecturerById(Integer id) {
        return lecturerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên ID: " + id));
    }
}
