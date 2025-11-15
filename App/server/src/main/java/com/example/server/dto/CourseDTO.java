package com.example.server.dto;

public class CourseDTO {
    
    private Integer CourseId;
    private String CourseCode;
    private String CourseName;
    private Integer Credit;
    private String Description;

    public CourseDTO() {
    }
    public CourseDTO(Integer courseId, String courseCode, String courseName, Integer credit, String description) {
        CourseId = courseId;
        CourseCode = courseCode;
        CourseName = courseName;
        Credit = credit;
        Description = description;
    }

    public Integer getCourseId() {
        return CourseId;
    }
    public void setCourseId(Integer courseId) {
        CourseId = courseId;
    }
    public String getCourseCode() {
        return CourseCode;
    }
    public void setCourseCode(String courseCode) {
        CourseCode = courseCode;
    }
    public String getCourseName() {
        return CourseName;
    }
    public void setCourseName(String courseName) {
        CourseName = courseName;
    }
    public Integer getCredit() {
        return Credit;
    }
    public void setCredit(Integer credit) {
        Credit = credit;
    }
    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }

}
