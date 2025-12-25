package com.example.client.HocVien.Models;

import com.google.gson.annotations.SerializedName;

public class HocVien_XemDiemDto {
    @SerializedName("fullName")
    private String fullName;

    @SerializedName("studentNumber")
    private String studentNumber;

    @SerializedName("courseName")
    private String courseName;

    @SerializedName("assignmentTitle")
    private String assignmentTitle; // Tên bài tập

    @SerializedName("grade")
    private Double grade; // Điểm số (Double vì có thể là 8.5)

    @SerializedName("submitStatus")
    private String submitStatus; // Trạng thái nộp

    // Getter
    public String getFullName() { return fullName; }
    public String getStudentNumber() { return studentNumber; }
    public String getCourseName() { return courseName; }
    public String getAssignmentTitle() { return assignmentTitle; }
    public Double getGrade() { return grade; }
    public String getSubmitStatus() { return submitStatus; }
}
