package com.example.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Class")
public class Class {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ClassId")
    private Integer ClassId;

    @Column(name = "CourseId")
    private Integer CourseId;

    @Column(name = "ClassCode")
    private String ClassCode;

    @Column(name = "Semester")
    private String Semester;

    @Column(name = "Capacity")
    private Integer Capacity;

    @Column(name = "LecturerId")
    private Integer LecturerId;

    @Column(name = "CreatedBy")
    private Integer CreatedBy;

    @Column(name = "CreatedAt")
    private String CreatedAt;

    public Class() {
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
        this.Capacity = capacity;
    }

    public Integer getLecturerId() {
        return LecturerId;
    }

    public void setLecturerId(Integer lecturerId) {
        this.LecturerId = lecturerId;
    }

    public Integer getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.CreatedBy = createdBy;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        this.CreatedAt = createdAt;
    }
}
