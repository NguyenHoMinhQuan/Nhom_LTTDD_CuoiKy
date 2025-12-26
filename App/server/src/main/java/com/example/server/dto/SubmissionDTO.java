package com.example.server.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubmissionDTO {
    private Integer submissionId;
    private Integer assignmentId;
    private Integer studentId;
    private String fileUrl;
    private String submittedAt;
    private Double grade;
    private String studentName;

    public SubmissionDTO() {
    }
    public SubmissionDTO(Integer submissionId, Integer assignmentId, Integer studentId,
                        String studentName, String fileUrl, LocalDateTime submittedAt, Double grade) {
    this.submissionId = submissionId;
    this.assignmentId = assignmentId;
    this.studentId = studentId;
    this.studentName = studentName;
    this.fileUrl = fileUrl;
    this.submittedAt = submittedAt != null ? submittedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    this.grade = grade;
}


    // Getters & Setters
    public Integer getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Integer submissionId) {
        this.submissionId = submissionId;
    }

    public Integer getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(String submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
    
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

}
