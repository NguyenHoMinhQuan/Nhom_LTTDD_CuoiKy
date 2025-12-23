package com.example.client.api;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class AdminResponse {

    // 1. Model khớp với AdminDTO.UserRow
    public static class User implements Serializable {
        @SerializedName("id") public int id;
        @SerializedName("fullName") public String fullName;
        @SerializedName("email") public String email;
        @SerializedName("department") public String department;
        @SerializedName("status") public String status; // "Hoạt động" hoặc "Khóa"
    }

    // 2. Model khớp với AdminDTO.CourseRow
    public static class Course implements Serializable {
        @SerializedName("id") public int id;
        @SerializedName("courseName") public String courseName;
        @SerializedName("lecturerName") public String lecturerName;
        @SerializedName("studentCount") public long studentCount;
    }

    // 3. Model khớp với AdminDTO.ClassRow
    public static class ClassItem implements Serializable {
        @SerializedName("courseName") public String courseName;
        @SerializedName("classCode") public String classCode;
        @SerializedName("room") public String room;
        @SerializedName("timeRange") public String timeRange; // Ví dụ: "07:00 - 09:00"

        // LƯU Ý: Backend DTO hiện tại chưa trả về "dayOfWeek".
        // Nếu muốn lọc theo ngày, bạn cần thêm dayOfWeek vào Backend.
        // Hiện tại Android sẽ hiển thị tất cả lớp học.
    }
}