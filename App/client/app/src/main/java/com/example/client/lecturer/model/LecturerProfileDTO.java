package com.example.client.lecturer.model;

public class LecturerProfileDTO {
    private Integer lecturerId;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String department;
    private String staffNumber;

    // Constructor rỗng
    public LecturerProfileDTO() {}

    // Getter và Setter (Bạn tự generate hoặc dùng Lombok nếu có cấu hình)
    public Integer getLecturerId() { return lecturerId; }
    public void setLecturerId(Integer lecturerId) { this.lecturerId = lecturerId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    // ... Các getter/setter khác
}