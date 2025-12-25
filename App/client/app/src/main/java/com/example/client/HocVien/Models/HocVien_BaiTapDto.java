package com.example.client.HocVien.Models;

import com.google.gson.annotations.SerializedName;

public class HocVien_BaiTapDto {
    @SerializedName("assignmentId")
    private Integer assignmentId;
    @SerializedName("assignmentTitle")
    private String assignmentTitle;
    @SerializedName("courseName")
    private String courseName;
    @SerializedName("classCode")
    private String classCode;
    @SerializedName("dueDate")
    private String dueDate; // Hạn nộp
    @SerializedName("submitStatus")
    private String submitStatus; // "Đã nộp" hoặc null/khác
    @SerializedName("grade")
    private Double grade;
    @SerializedName("username")
    private String username;

    // Getters
    public Integer getAssignmentId() { return assignmentId; }
    public String getAssignmentTitle() { return assignmentTitle; }
    public String getCourseName() { return courseName; }
    public String getClassCode() { return classCode; }
    public String getDueDate() { return dueDate; }
    public String getSubmitStatus() { return submitStatus; }
    public Double getGrade() { return grade; }
    public String getUsername() { return username; }
}
