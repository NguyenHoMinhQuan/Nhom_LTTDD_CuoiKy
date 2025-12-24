package com.example.client.lecturer.model;

import java.io.Serializable;

public class AssignmentDTO implements Serializable {
    private Integer assignmentId;
    private Integer classId;
    private String title;
    private String description;
    private String dueDate;
    private String createdAt;
    public AssignmentDTO(){}
    public AssignmentDTO(Integer classId, String title, String description, String dueDate, String createdAt){
        this.classId=classId;
        this.title=title;
        this.description=description;
        this.dueDate=dueDate;
        this.createdAt=createdAt;
    }
    public Integer getAssignmentId(){return assignmentId;}
    public Integer getClassId(){return classId;}
    public String getTitle(){return title;}
    public String getDescription(){return description;}
    public String getDueDate(){return dueDate;}
    public String getCreatedAt(){return createdAt;}

    public void setAssignmentId(Integer assigmentId) {
        this.assignmentId = assigmentId;
    }
    public  void setClassId(Integer classId){
        this.classId=classId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
