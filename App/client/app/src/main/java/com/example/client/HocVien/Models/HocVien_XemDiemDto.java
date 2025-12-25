package com.example.client.HocVien.Models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable; // Bắt buộc import để truyền Intent

public class HocVien_XemDiemDto implements Serializable {

    // --- NHÓM 1: Thông tin Bài tập & Điểm ---
    @SerializedName("assignmentId")
    private Integer assignmentId;

    @SerializedName("assignmentTitle")
    private String assignmentTitle;

    @SerializedName("dueDate")
    private String dueDate; // Hạn nộp

    @SerializedName("submitStatus")
    private String submitStatus; // Trạng thái "Đã nộp" / "Chưa nộp"

    @SerializedName("grade")
    private Double grade; // Điểm số

    // --- NHÓM 2: Thông tin Lớp học ---
    @SerializedName("courseName")
    private String courseName;

    @SerializedName("classCode")
    private String classCode;

    @SerializedName("semester")
    private String semester;

    // --- NHÓM 3: Thông tin Sinh viên ---
    @SerializedName("username")
    private String username;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("studentNumber")
    private String studentNumber;


    // =========================================================
    // GETTER (Đã gộp và loại bỏ trùng lặp)
    // =========================================================

    public Integer getAssignmentId() { return assignmentId; }

    public String getAssignmentTitle() { return assignmentTitle; }

    public String getDueDate() { return dueDate; }

    public String getSubmitStatus() { return submitStatus; }

    public Double getGrade() { return grade; }

    public String getCourseName() { return courseName; }

    public String getClassCode() { return classCode; }

    public String getSemester() { return semester; }

    public String getUsername() { return username; }

    public String getFullName() { return fullName; }

    public String getStudentNumber() { return studentNumber; }
}