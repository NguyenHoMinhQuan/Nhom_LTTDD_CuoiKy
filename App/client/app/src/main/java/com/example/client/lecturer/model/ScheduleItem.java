package com.example.client.lecturer.model;

import java.io.Serializable;

public class ScheduleItem implements Serializable {
    private Integer scheduleId;
    private Integer classId;
    private String dayOfWeek;
    private String courseName;
    private String startTime;
    private String endTime;
    private String room;

    public ScheduleItem(Integer scheduleId, Integer classId, String dayOfWeek, String courseName, String startTime, String endTime, String room) {
        this.scheduleId = scheduleId;
        this.classId = classId;
        this.dayOfWeek = dayOfWeek;
        this.courseName = courseName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
