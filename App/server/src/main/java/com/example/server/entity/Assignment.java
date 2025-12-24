package com.example.server.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "[Assignment]")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AssignmentId")
    private Integer AssignmentId;
    @Column(name = "ClassId")
    private Integer classId;
    @Column(name = "Title")
    private String Title;
    @Column(name = "Description")
    private String Description;
    @Column(name = "DueDate")
    private LocalDateTime DueDate;
    @Column(name = "CreatedAt")
    private LocalDateTime CreatedAt;

    public Assignment() {
    }

    public Integer getAssignmentId() {
        return AssignmentId;
    }

    public Integer getClassId() {
        return classId;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public LocalDateTime getDueDate() {
        return DueDate;
    }

    public LocalDateTime getCreatedAt() {
        return CreatedAt;
    }

    public void setAssignmentId(Integer assignmentId) {
        AssignmentId = assignmentId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setDueDate(LocalDateTime dueDate) {
        DueDate = dueDate;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        CreatedAt = createdAt;
    }

}
