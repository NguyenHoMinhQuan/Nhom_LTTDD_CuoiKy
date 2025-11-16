    package com.example.server.dto;

    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.AllArgsConstructor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class LoginRequest {
        private String username;
        private String password; // Đây là mật khẩu gốc (Plain text) từ Client.
    }