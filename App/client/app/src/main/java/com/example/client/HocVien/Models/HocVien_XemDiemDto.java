package com.example.client.HocVien.Models; // Kiểm tra lại package của bạn

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class HocVien_XemDiemDto implements Serializable {

    @SerializedName("assignmentId")
    private Integer assignmentId;

    @SerializedName("assignmentTitle")
    private String assignmentTitle; // Tên bài tập

    @SerializedName("dueDate")
    private String dueDate; // Hạn nộp

    @SerializedName("submitStatus")
    private String submitStatus; // Trạng thái nộp

    @SerializedName("grade")
    private Double grade; // Điểm số

    @SerializedName("courseName")
    private String courseName;

    @SerializedName("classCode")
    private String classCode;

    @SerializedName("username")
    private String username;
    @SerializedName("fullName")
    private String fullName;

    @SerializedName("studentNumber")
    private String studentNumber;

    @SerializedName("semester")
    private String semester;

    public Integer getAssignmentId() { return assignmentId; }

    public String getAssignmentTitle() { return assignmentTitle; }

    public String getDueDate() { return dueDate; }

    public String getSubmitStatus() { return submitStatus; }

    public Double getGrade() { return grade; }

    public String getCourseName() { return courseName; }

    public String getClassCode() { return classCode; }

    public String getUsername() { return username; }
    public String getFullName() { return fullName; }

    public String getStudentNumber() { return studentNumber; }

    public String getSemester() { return semester; }
}