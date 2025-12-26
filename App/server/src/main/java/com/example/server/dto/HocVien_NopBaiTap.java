package com.example.server.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HocVien_NopBaiTap {
    private String username;    // Tài khoản sinh viên
    private String classCode;   // Mã lớp (ví dụ: CT101_B)
    private Integer assignmentId; // ID của bài tập
    private String fileUrl;
}
