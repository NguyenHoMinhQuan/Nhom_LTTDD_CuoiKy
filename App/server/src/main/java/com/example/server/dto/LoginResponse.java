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
    private UserDTO userProfile; // Dữ liệu User an toàn
}