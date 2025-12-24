package com.example.client.api;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class AdminResponse {

    // 1. USER: Map từ "userId", "isActive" của Server sang "id", "status" của Client
    public static class User implements Serializable {
        @SerializedName("userId")
        public Integer id;

        @SerializedName("username") // Thêm trường này để hiển thị trên bảng
        public String username;

        @SerializedName("fullName")
        public String fullName;

        @SerializedName("email")
        public String email;

        @SerializedName("department")
        public String department;

        @SerializedName("role")
        public String role;

        @SerializedName("isActive")
        public Boolean isActive;
    }

    public static class UserRequest {
        public Integer id;
        public String username; // ĐÂY LÀ DÒNG BẠN ĐANG THIẾU (Sửa lỗi Cannot resolve symbol)
        public String fullName;
        public String email;
        public String password;
        public String department;
        public Integer roleId;
        public Integer status;

        public UserRequest() {}
    }

    // 2. COURSE: Map từ "courseId" sang "id"
    public static class Course implements Serializable {
        @SerializedName("courseId") // Server gửi "courseId"
        public Integer id;

        @SerializedName("courseCode") // Thêm code cho đầy đủ
        public String courseCode;

        @SerializedName("courseName")
        public String courseName;

        @SerializedName("lecturerName")
        public String lecturerName;

        @SerializedName("studentCount")
        public Long studentCount;

        @SerializedName("credit")
        public Integer credit;
    }

    // 3. CLASS ITEM
    public static class ClassItem implements Serializable {
        @SerializedName("courseName") public String courseName;
        @SerializedName("classCode") public String classCode;
        @SerializedName("room") public String room;
        @SerializedName("startTime") public String startTime; // Từ Server
        @SerializedName("endTime") public String endTime;     // Từ Server

        // Hàm bổ trợ để lấy chuỗi thời gian hiển thị
        // Trong AdminResponse.ClassItem
        public String getTimeRange() {
            return (startTime != null && endTime != null) ? startTime + " - " + endTime : "N/A";
        }
    }
    // ==================================================
    // 2. KHÓA HỌC (COURSE) - ĐÃ CẬP NHẬT THEO API MỚI
    // ==================================================
    public static class CourseRow implements Serializable {
        @SerializedName("id") // Khớp với trường 'id' từ Server CourseRow
        public Integer id;

        @SerializedName("maMon") // Khớp với 'maMon' (CourseCode)
        public String maMon;

        @SerializedName("tenMon") // Khớp với 'tenMon' (CourseName)
        public String tenMon;

        @SerializedName("tinChi") // Khớp với 'tinChi' (Credit)
        public Integer tinChi;

        @SerializedName("moTa") // Khớp với 'moTa' (Description)
        public String moTa;
    }

    public static class CourseRequest {
        public Integer id;
        public String maMon;
        public String tenMon;
        public Integer tinChi;
        public String moTa;
        public CourseRequest() {}
    }
    // ==================================================
    // 3. LỊCH HỌC (CLASS SCHEDULE) - ĐÃ CẬP NHẬT THEO API MỚI
    // ==================================================
    public static class ClassRow implements Serializable {

        @SerializedName("scheduleId")
        public Integer id;              // ID lịch học (Dùng để Sửa/Xóa)

        @SerializedName("courseName")
        public String tenMon;           // Tên môn học (VD: Lập trình Android)

        @SerializedName("classCode")
        public String maLop;            // Mã lớp (VD: D19CQCN01)

        @SerializedName("lecturerName")
        public String giangVien;        // Tên giảng viên

        @SerializedName("room")
        public String phong;            // Phòng học

        @SerializedName("startTime")
        public String gioBD;            // Giờ bắt đầu (VD: "07:00:00")

        @SerializedName("endTime")
        public String gioKT;            // Giờ kết thúc (VD: "09:00:00")

        @SerializedName("courseCode")
        public String maMon;            // Mã môn (VD: INT1332) - Dùng để đổ dữ liệu vào ô nhập

        @SerializedName("dayOfWeek")
        public Integer thu;             // Thứ (2, 3, 4...) - Dùng để set Spinner

        @SerializedName("credit")
        public Integer tinChi;          // Số tín chỉ (nếu cần hiển thị thêm)

        @SerializedName("semester")
        public String hocKy;            // Học kỳ
    }

    // ==================================================
    // 4. CLASS REQUEST - Dùng để GỬI dữ liệu (POST/PUT)
    // ==================================================
    public static class ClassRequest {
        public Integer scheduleId;   // Null nếu Thêm mới, có giá trị nếu Sửa
        public String courseCode;    // Mã môn (Khớp với edtMaMon trong Activity)
        public String classCode;     // Mã lớp
        public String lecturerName;  // Tên giảng viên
        public String room;          // Phòng học
        public Integer dayOfWeek;    // Thứ (2-8)
        public String startTime;     // Giờ bắt đầu ("HH:mm")
        public String endTime;       // Giờ kết thúc ("HH:mm")
    }
    // ==================================================
    // 5. THÔNG BÁO (ANNOUNCEMENT) - ĐÃ TÍCH HỢP
    // ==================================================
    public static class Announcement implements Serializable {
        @SerializedName("announcementId") public Integer id;
        @SerializedName("title") public String title;
        @SerializedName("body") public String body;
        @SerializedName("authorId") public String authorId;
        @SerializedName("isGlobal") public Boolean isGlobal;

        @SerializedName("targetClassId") // <-- THÊM DÒNG NÀY
        public String targetClassId;

        @SerializedName("createdAt") public String createdAt;

        public Announcement() {}
    }
}