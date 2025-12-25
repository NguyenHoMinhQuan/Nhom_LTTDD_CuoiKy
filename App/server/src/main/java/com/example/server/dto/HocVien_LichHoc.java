package com.example.server.dto;
import java.time.LocalTime;
public interface HocVien_LichHoc {
    String getUsername();
    String getStudentNumber();
    String getFullName();
    String getClassCode();
    String getCourseCode();
    String getCourseName();
    Integer getDayOfWeek(); // 2, 3, 4, ...
    LocalTime getStartTime();
    LocalTime getEndTime();
    String getRoom();
}
