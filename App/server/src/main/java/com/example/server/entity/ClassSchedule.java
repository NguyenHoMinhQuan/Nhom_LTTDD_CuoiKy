package com.example.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalTime;

@Entity
@Table(name = "ClassSchedule")
public class ClassSchedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ScheduleId")
    private Integer ScheduleId;

    @Column(name = "ClassId")
    private Integer ClassId;

    @Column(name = "DayOfWeek")
    private Integer DayOfWeek;

    @Column(name = "StartTime")
    private LocalTime StartTime;

    @Column(name = "EndTime")
    private LocalTime EndTime;

    @Column(name = "Room")
    private String Room;

    public ClassSchedule() {
    }

    public Integer getScheduleId() {
        return ScheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.ScheduleId = scheduleId;
    }

    public Integer getClassId() {
        return ClassId;
    }

    public void setClassId(Integer classId) {
        this.ClassId = classId;
    }

    public Integer getDayOfWeek() {
        return DayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.DayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return StartTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.StartTime = startTime;
    }

    public LocalTime getEndTime() {
        return EndTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.EndTime = endTime;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        this.Room = room;
    }

}
