package com.example.server.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface HocVien_SoYeuLiLich {
    Integer getUserId();
    String getUsername();
    String getEmail();
    Boolean getIsActive();
    LocalDateTime getCreatedAt();

    String getStudentNumber();
    String getFullName();
    LocalDate getDateOfBirth();
    String getFaculty();
    String getYear();
}
