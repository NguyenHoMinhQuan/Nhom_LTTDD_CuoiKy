package com.example.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Course")
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CourseId")
    private Integer CourseId;

    @Column(name = "CourseCode")
    private String CourseCode;

    @Column(name = "CourseName")
    private String CourseName;

    @Column(name = "Credit")
    private Integer Credit;

    @Column(name = "Description")
    private String Description;

    public Course() {
    }

    public Integer getCourseId() {
        return CourseId;
    }

    public void setCourseId(Integer courseId) {
        this.CourseId = courseId;
    }

    public String getCourseCode() {
        return CourseCode;
    }

    public void setCourseCode(String courseCode) {
        this.CourseCode = courseCode;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        this.CourseName = courseName;
    }

    public Integer getCredit() {
        return Credit;
    }

    public void setCredit(Integer credit) {
        this.Credit = credit;
    }

    public String getDescription() {
        return Description;
    }   

    public void setDescription(String description) {
        this.Description = description;
    }

}
