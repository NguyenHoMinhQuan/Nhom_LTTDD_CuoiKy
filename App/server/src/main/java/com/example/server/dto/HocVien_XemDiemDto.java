package com.example.server.dto;

import java.time.LocalDateTime;

public interface HocVien_XemDiemDto {
    String getUsername();
    String getStudentNumber();
    String getFullName();
    String getClassCode();
    String getSemester();
    String getCourseCode();
    String getCourseName();
    Integer getAssignmentId();
    String getAssignmentTitle();

    LocalDateTime getDueDate();
    LocalDateTime getSubmittedAt();
    Double getGrade();
    String getSubmitStatus();
}
