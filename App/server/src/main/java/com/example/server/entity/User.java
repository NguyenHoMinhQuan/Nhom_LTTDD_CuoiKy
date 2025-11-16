package com.example.server.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "[User]") // Khắc phục lỗi reserved keyword
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId") // Bắt buộc phải có để khớp PascalCase
    private Integer userId;

    @Column(name = "Username", unique = true)
    private String username;

    @Column(name = "Email", unique = true)
    private String email;

    @Column(name = "PasswordHash") // Chứa chuỗi BCrypt
    private String passwordHash;

    @Column(name = "RoleId")
    private Byte roleId;

    @Column(name = "IsActive")
    private Boolean isActive;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "LastLoginAt")
    private LocalDateTime lastLoginAt;

    public User() {
    }
}