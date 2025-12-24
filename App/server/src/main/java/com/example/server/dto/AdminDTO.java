package com.example.server.dto;

import java.io.Serializable;
import java.time.LocalTime;
public class AdminDTO {

    // ================= USER =================
    public static class UserRow {
        public Integer userId;
        public String username;
        public String fullName;
        public String email;
        public String department;
        public String role;      // <--- SỬA: Đổi byte -> String (để nhận RoleName từ Query)
        public Boolean isActive;

        // Constructor phải khớp với thứ tự select trong Repository
        public UserRow(Integer userId, String username, String fullName, String email,
                       String department, String role, Boolean isActive) {
            this.userId = userId;
            this.username = username;
            this.fullName = fullName;
            this.email = email;
            this.department = department;
            this.role = role; 
            this.isActive = isActive;
        }
    }

    // ================= COURSE =================
    public static class CourseRow {
        public Integer id;
        public String maMon;
        public String tenMon;
        public Integer tinChi;
        public String moTa;

        public CourseRow(Integer id, String maMon, String tenMon, Integer tinChi, String moTa) {
            this.id = id;
            this.maMon = maMon;
            this.tenMon = tenMon;
            this.tinChi = tinChi;
            this.moTa = moTa;
        }
    }

    // ================= CLASS =================
    public static class ClassRow {
        public Integer scheduleId;
        public String courseCode;
        public String courseName;
        public String classCode;
        public String lecturerName;
        public String room;
        public Integer dayOfWeek;
        public LocalTime startTime;
        public LocalTime endTime;

        public ClassRow(Integer scheduleId, String courseCode, String courseName,
                        String classCode, String lecturerName, String room,
                        Integer dayOfWeek, LocalTime startTime, LocalTime endTime) {
            this.scheduleId = scheduleId;
            this.courseCode = courseCode;
            this.courseName = courseName;
            this.classCode = classCode;
            this.lecturerName = lecturerName;
            this.room = room;
            this.dayOfWeek = dayOfWeek;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }

    // ================= REQUEST =================
    // Dùng để Thêm/Sửa: Nên dùng ID cho chính xác
    public static class UserRequest {
        public Integer id;
        public String email;
        public String username;
        public String password;
        public String fullName;
        public String department;   // Khoa hoặc Bộ môn
        public Integer roleId;      // <--- SỬA: Dùng Integer (2=SV, 3=GV) thay vì String
        public Integer status;      // <--- SỬA: Dùng Integer (1=Active, 0=Lock) thay vì String
    }
    public static class CourseRequest {
        public Integer id;
        public String maMon;
        public String tenMon;
        public Integer tinChi;
        public String moTa;
    }
    public static class ClassRequest {
        public Integer scheduleId;   // Null nếu là Thêm mới
        public String classCode;     // Mã lớp (để tìm ClassId)
        public String lecturerName;  // Tên GV (để tìm LecturerId update vào bảng Class)
        public String room;
        public Integer dayOfWeek;
        public String startTime;     // Nhận chuỗi "HH:mm" từ Client
        public String endTime;       // Nhận chuỗi "HH:mm" từ Client
    }
    // ==========================================
    // DTO CHO QUẢN LÝ THÔNG BÁO (ANNOUNCEMENT)
    // ==========================================
    public static class AnnouncementRequest implements Serializable {
        public Integer announcementId; // Dùng cho Sửa/Xóa
        public String title;
        public String body;
        public String authorId;     // Mặc định là ADMIN
        public Boolean isGlobal;    // true = Toàn hệ thống
        public String targetClassId;// Nếu gửi riêng cho lớp nào (lưu chuỗi "ALL" hoặc Mã lớp)
        
        // Các trường này Server tự tạo, Client không cần gửi lên
        public String createdAt;
        public String updatedAt;
    }
}