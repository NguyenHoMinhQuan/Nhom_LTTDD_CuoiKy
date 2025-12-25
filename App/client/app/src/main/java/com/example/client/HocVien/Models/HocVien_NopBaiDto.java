package com.example.client.Models; // Hoặc package của bạn

import com.google.gson.annotations.SerializedName;

public class HocVien_NopBaiDto {
    @SerializedName("username")
    private String username;

    @SerializedName("classCode")
    private String classCode;

    @SerializedName("assignmentId")
    private Integer assignmentId;

    @SerializedName("fileUrl")
    private String fileUrl;

    public HocVien_NopBaiDto(String username, String classCode, Integer assignmentId, String fileUrl) {
        this.username = username;
        this.classCode = classCode;
        this.assignmentId = assignmentId;
        this.fileUrl = fileUrl;
    }
}