// File: dto/LoginResponse.java
package com.example.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String username;
    private boolean success;
    private String message;
    private String token; // JWT token
    private UserDTO userProfile; // Dữ liệu User an toàn
}