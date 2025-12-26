package com.example.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DanhGiaDto {
    // 1. Class ID (Giữ nguyên)
    @JsonProperty("classId")
    private Integer classId;

    // 2. SỬA: Đổi "sinhVienId" thành "studentId" để khớp Client
    @JsonProperty("studentId") 
    private Integer sinhVienId;

    // 3. SỬA: Đổi "soSao" thành "rating" để khớp Client
    @JsonProperty("rating")
    private Integer soSao;

    // 4. SỬA: Đổi "nhanXet" thành "comment" để khớp Client
    @JsonProperty("comment")
    private String nhanXet;
}