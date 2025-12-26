package com.example.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DanhGiaDto {
   
    @JsonProperty("classId")
    private Integer classId;

    @JsonProperty("studentId") 
    private Integer sinhVienId;

    @JsonProperty("rating")
    private Integer soSao;

    @JsonProperty("comment")
    private String nhanXet;
}