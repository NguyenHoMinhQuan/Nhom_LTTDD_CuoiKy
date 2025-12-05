package com.example.client.HocVien;

public class MyCourses {
    private String name;        // Tên khóa học
    private String description; // Mô tả chi tiết
    private int imageResId;     // ID của hình ảnh

    public MyCourses(String name, String description, int imageResId) {
        this.name = name;
        this.description = description;
        this.imageResId = imageResId;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getImageResId() { return imageResId; }

}
