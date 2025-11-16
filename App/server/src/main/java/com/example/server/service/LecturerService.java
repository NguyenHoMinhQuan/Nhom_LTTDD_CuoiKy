package com.example.server.service;

import com.example.server.entity.Lecturer;

import java.util.List;

public interface LecturerService {
    List<Lecturer> getAllLecturers();

    Lecturer getLecturerById(Integer id);
}
