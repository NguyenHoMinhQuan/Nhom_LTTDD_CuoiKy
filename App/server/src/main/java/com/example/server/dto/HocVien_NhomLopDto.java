package com.example.server.dto;

import java.time.LocalDateTime;

public interface HocVien_NhomLopDto {
    Integer getUserId();
    String getUsername();
    String getStudentName();
    Integer getRegistrationId();
    String getStatus();
    LocalDateTime getRegisteredAt();
    String getClassCode();
    String getSemester();
    String getCourseCode();
    String getCourseName();
    String getLecturerName();
}
