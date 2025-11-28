package com.example.client.HocVien;

public class BuyCourses {
    private String name;
    private String price;
    private String duration; // Ví dụ: "3 tháng"
    private int imageResId;

    public BuyCourses(String name, String price, String duration, int imageResId) {
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.imageResId = imageResId;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getDuration() { return duration; }
    public int getImageResId() { return imageResId; }
}
