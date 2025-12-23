package com.example.server.dto;

import java.time.LocalTime; // Nhớ import cái này

public class AdminDTO {

    // 1. DTO cho User (Giữ nguyên)
    public static class UserRow {
        public Integer id;
        public String fullName;
        public String email;
        public String department;
        public String status;

        public UserRow(Integer id, String fullName, String email, String department, Boolean isActive) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.department = department;
            this.status = (isActive != null && isActive) ? "Hoạt động" : "Khóa";
        }
    }

    // 2. DTO cho Course (Giữ nguyên)
    public static class CourseRow {
        public Integer id;
        public String courseName;
        public String lecturerName;
        public Long studentCount;

        public CourseRow(Integer id, String courseName, String lecturerName, Long studentCount) {
            this.id = id;
            this.courseName = courseName;
            this.lecturerName = lecturerName;
            this.studentCount = studentCount;
        }
    }

    // 3. DTO cho Class Schedule (CẬP NHẬT KIỂU GIỜ)
    public static class ClassRow {
        public String courseName;
        public String classCode;
        public String room;
        public String timeRange;

        // Lưu ý: Tham số start và end phải là LocalTime
        public ClassRow(String courseName, String classCode, String room, LocalTime start, LocalTime end) {
            this.courseName = courseName;
            this.classCode = classCode;
            this.room = room;
            // Xử lý chuỗi giờ: lấy 07:00 từ 07:00:00
            String s = (start != null) ? start.toString() : "00:00";
            String e = (end != null) ? end.toString() : "00:00";
            if(s.length() > 5) s = s.substring(0, 5);
            if(e.length() > 5) e = e.substring(0, 5);
            
            this.timeRange = s + " - " + e;
        }
    }
}