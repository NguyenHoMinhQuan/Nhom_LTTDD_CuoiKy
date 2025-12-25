package com.example.server.dto;

import java.time.LocalTime;

public class ClassScheduleDTO {
    
    // Sử dụng camelCase cho tất cả các trường
    private Integer scheduleId; 
    private String courseName;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String room;
    private Integer classId; 

    
    public ClassScheduleDTO() {
    }

    // --- Getters và Setters (Đã sửa để khớp với camelCase) ---
    
    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }
}