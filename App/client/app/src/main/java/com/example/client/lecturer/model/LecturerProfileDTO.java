package com.example.client.lecturer.model;

public class LecturerProfileDTO {
    private Integer lecturerId;
    private String username;
    private String email;
    private String password;
    private String fullName;

    // 🟢 THÊM 2 TRƯỜNG NÀY VÀO (để đồng bộ với Server)
    private String department;
    private String staffNumber;

    public LecturerProfileDTO() {}

    // Getter và Setter
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

    // 🟢 THÊM GETTER/SETTER CHO 2 TRƯỜNG MỚI
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getStaffNumber() { return staffNumber; }
    public void setStaffNumber(String staffNumber) { this.staffNumber = staffNumber; }
}