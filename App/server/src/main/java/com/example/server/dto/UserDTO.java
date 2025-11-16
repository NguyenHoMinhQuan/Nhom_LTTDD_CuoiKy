    // File: dto/UserDTO.java
    package com.example.server.dto;

    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.AllArgsConstructor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class UserDTO {
        private Integer userId;
        private String username;
        private String email;
        private String role; // Tên Role đã ánh xạ
    }