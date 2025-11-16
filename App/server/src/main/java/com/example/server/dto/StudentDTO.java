package com.example.server.dto;

public class StudentDTO {
    private Integer studentId;
    private String studentNumber;
    private String fullName;
    private String faculty;
    private Integer year;

    public StudentDTO() {
    }

    public StudentDTO(Integer studentId, String studentNumber, String fullName, String faculty, Integer year) {
        this.studentId = studentId;
        this.studentNumber = studentNumber;
        this.fullName = fullName;
        this.faculty = faculty;
        this.year = year;
    }

    // Getters & Setters
    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
