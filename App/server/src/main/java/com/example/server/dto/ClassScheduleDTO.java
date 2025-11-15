package com.example.server.dto;

import java.time.LocalTime;

public class ClassScheduleDTO {
    
    private Integer ScheduleId;
    private Integer ClassId;
    private Integer DayOfWeek;
    private LocalTime StartTime;
    private LocalTime EndTime;
    private String Room;
    
    public ClassScheduleDTO() {
    }

    public Integer getScheduleId() {
        return ScheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        ScheduleId = scheduleId;
    }

    public Integer getClassId() {
        return ClassId;
    }

    public void setClassId(Integer classId) {
        ClassId = classId;
    }

    public Integer getDayOfWeek() {
        return DayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        DayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return StartTime;
    }

    public void setStartTime(LocalTime startTime) {
        StartTime = startTime;
    }

    public LocalTime getEndTime() {
        return EndTime;
    }

    public void setEndTime(LocalTime endTime) {
        EndTime = endTime;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }
    
    
}
