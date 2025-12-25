package com.example.server.dto;

import lombok.Data;

@Data
public class LecturerProfileDTO {
    private Integer lecturerId;
    private String username;    // Từ bảng User
    private String email;       // Từ bảng User
    private String password;    // Từ bảng User (để đổi mật khẩu)
    private String fullName;    // Từ bảng Lecturer
    private String department;  // Từ bảng Lecturer
    private String staffNumber; // Từ bảng Lecturer
}
