package com.example.client.HocVien.Models;

import com.google.gson.annotations.SerializedName;

public class LichHocSinhVienModel {
    @SerializedName("dayOfWeek")
    private Integer dayOfWeek; // 1, 2, 3, ... (Chủ nhật có thể là 1 hoặc 8 tùy quy ước server)

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("username")
    private String username;

    @SerializedName("studentNumber")
    private String studentNumber;

    @SerializedName("courseName")
    private String courseName;

    @SerializedName("courseCode")
    private String courseCode;

    @SerializedName("classCode")
    private String classCode;

    @SerializedName("room")
    private String room;

    @SerializedName("startTime")
    private String startTime; // Dạng chuỗi "07:00:00"

    @SerializedName("endTime")
    private String endTime;   // Dạng chuỗi "09:00:00"

    // --- Constructor ---
    public LichHocSinhVienModel() {
    }

    // --- Getter Methods ---

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public String getRoom() {
        return room;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
