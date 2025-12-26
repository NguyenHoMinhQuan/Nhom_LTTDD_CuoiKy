package com.example.client.HocVien.Models;

import com.google.gson.annotations.SerializedName;

public class SoYeuLyLichModel {
    @SerializedName("userId")
    private Integer userId;

    @SerializedName("username")
    private String username;

    @SerializedName("studentNumber")
    private String studentNumber;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("faculty")
    private String faculty;

    @SerializedName("year")
    private String year;

    // JSON trả về null, nên để String là an toàn nhất.
    // Nếu sau này có dữ liệu ngày tháng dạng chuỗi, nó vẫn hứng được.
    @SerializedName("dateOfBirth")
    private String dateOfBirth;

    @SerializedName("isActive")
    private Boolean isActive;

    @SerializedName("createdAt")
    private String createdAt;

    // --- Constructor rỗng (Cần thiết cho Gson) ---
    public SoYeuLyLichModel() {
    }

    // --- Getter Methods (Chỉ cần Get để hiển thị) ---

    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getYear() {
        return year;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public Boolean getActive() {
        return isActive;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
