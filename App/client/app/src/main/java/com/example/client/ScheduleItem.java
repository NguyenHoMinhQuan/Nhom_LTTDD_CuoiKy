package com.example.client;

import java.io.Serializable;

public class ScheduleItem implements Serializable {
    private String dayOfWeek;
    private String courseName;
    private String startTime;
    private String endTime;
    private String room;
    private String teacherName; // Thêm thông tin giảng viên để đầy đủ

    // Constructor
    public ScheduleItem(String dayOfWeek, String courseName, String startTime, String endTime, String room, String teacherName) {
        this.dayOfWeek = dayOfWeek;
        this.courseName = courseName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.teacherName = teacherName;
    }

    // Getters
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getRoom() {
        return room;
    }

    public String getTeacherName() {
        return teacherName;
    }
}
