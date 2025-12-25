package com.example.client.HocVien.Models;

import com.google.gson.annotations.SerializedName;

public class HocVien_NhomLopDto {

    // --- THÊM DÒNG NÀY ---
    @SerializedName("classId") // Tên này phải khớp với JSON từ server trả về
    private int classId;
    // ---------------------
    @SerializedName("courseName")
    private String courseName;

    @SerializedName("courseCode")
    private String courseCode;

    @SerializedName("lecturerName")
    private String lecturerName;

    @SerializedName("classCode")
    private String classCode;

    @SerializedName("semester")
    private String semester;

    // Getter
    public String getCourseName() { return courseName; }
    public String getCourseCode() { return courseCode; }
    public String getLecturerName() { return lecturerName; }
    public String getClassCode() { return classCode; }
    public String getSemester() { return semester; }

    // --- THÊM HÀM GETTER NÀY ---
    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }


}
