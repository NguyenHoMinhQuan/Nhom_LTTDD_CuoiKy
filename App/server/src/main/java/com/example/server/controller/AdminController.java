package com.example.server.controller; // <--- SỬA PACKAGE

import java.util.List; // <--- SỬA IMPORT

import org.springframework.beans.factory.annotation.Autowired; // <--- SỬA IMPORT
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.dto.AdminDTO;
import com.example.server.repository.AdminRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/users")
    public List<AdminDTO.UserRow> getUsers() {
        return adminRepository.getAdminUsers();
    }

    @GetMapping("/courses")
    public List<AdminDTO.CourseRow> getCourses() {
        return adminRepository.getAdminCourses();
    }

    @GetMapping("/classes")
    public List<AdminDTO.ClassRow> getClasses() {
        return adminRepository.getAdminClasses();
    }
}