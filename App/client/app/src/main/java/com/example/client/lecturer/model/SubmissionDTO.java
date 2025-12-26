package com.example.client.lecturer.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SubmissionDTO implements Serializable {
    private Integer submissionId;
    private Integer assignmentId;
    private Integer studentId;
    private String studentName;
    private String fileUrl;
    private String submittedAt;
    private Double grade;

    // getter & setter
    public Integer getSubmissionId() { return submissionId; }
    public void setSubmissionId(Integer submissionId) { this.submissionId = submissionId; }

    public Integer getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Integer assignmentId) { this.assignmentId = assignmentId; }

    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(String submittedAt) { this.submittedAt = submittedAt; }

    public Double getGrade() { return grade; }
    public void setGrade(Double grade) { this.grade = grade; }
}
