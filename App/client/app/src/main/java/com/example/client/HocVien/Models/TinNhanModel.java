package com.example.client.HocVien.Models;

import com.google.gson.annotations.SerializedName;

public class TinNhanModel {

    // --- Các trường dùng để HIỂN THỊ (GET) ---
    // Tên trường trong @SerializedName phải khớp 100% với JSON Server trả về
    @SerializedName("messageId")
    private Integer messageId;

    @SerializedName("content")
    private String content; // Nội dung tin nhắn

    @SerializedName("senderName")
    private String senderName; // Tên hiển thị (VD: Nguyễn Văn A)

    @SerializedName("senderUsername")
    private String senderUsername; // Username (VD: student1) - dùng để check tin nhắn của mình

    @SerializedName("sentAt")
    private String sentAt; // Thời gian gửi

    // --- Các trường dùng để GỬI (POST) ---
    @SerializedName("classId")
    private Integer classId;

    @SerializedName("senderId")
    private Integer senderId;

    // Constructor dùng khi GỬI tin nhắn
    public TinNhanModel(Integer classId, Integer senderId, String content) {
        this.classId = classId;
        this.senderId = senderId;
        this.content = content;
    }

    // Constructor đầy đủ (dùng khi Gson parse dữ liệu từ Server)
    public TinNhanModel() {
    }

    // Getter
    public String getContent() {
        return content;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getSentAt() {
        return sentAt;
    }

    public Integer getSenderId() {
        return senderId;
    }
}