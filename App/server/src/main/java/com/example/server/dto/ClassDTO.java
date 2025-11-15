package com.example.server.dto;

public class ClassDTO {
    
    private Integer ClassId;

    private Integer CourseId;

    private String ClassCode;

    private String Semester;

    private Integer Capacity;

    private Integer LecturerId;

    private Integer CreatedBy;

    private String CreatedAt;

    public ClassDTO() {
    }

    public Integer getClassId() {
        return ClassId;
    }

    public void setClassId(Integer classId) {
        this.ClassId = classId;
    }

    public Integer getCourseId() {
        return CourseId;
    }

    public void setCourseId(Integer courseId) {
        this.CourseId = courseId;
    }

    public String getClassCode() {
        return ClassCode;
    }

    public void setClassCode(String classCode) {
        this.ClassCode = classCode;
    }

    public String getSemester() {
        return Semester;
    }

    public void setSemester(String semester) {
        this.Semester = semester;
    }

    public Integer getCapacity() {
        return Capacity;
    }

    public void setCapacity(Integer capacity) {
        Capacity = capacity;
    }

    public Integer getLecturerId() {
        return LecturerId;
    }

    public void setLecturerId(Integer lecturerId) {
        LecturerId = lecturerId;
    }

    public Integer getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(Integer createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }
}
