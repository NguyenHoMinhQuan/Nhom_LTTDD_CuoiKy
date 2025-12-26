package com.example.client.lecturer.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ClassDTO implements Serializable {
    @SerializedName("classId")
    private Integer classId;

    @SerializedName("classCode")
    private String classCode;

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    @NonNull
    @Override
    public String toString() {
        return classCode;
    }
}
