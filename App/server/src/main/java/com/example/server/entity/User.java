package com.example.server.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

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

    @Column(name = "Password") 
    private String password;

    @Column(name = "RoleId")
    private Byte roleId;
    // Hibernate đủ thông minh để join cột TINYINT (User) với INT (Role)
    // Miễn là giá trị nằm trong khoảng cho phép.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RoleId", insertable = false, updatable = false)
    private Role role;

    @Column(name = "IsActive")
    private Boolean isActive;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "LastLoginAt")
    private LocalDateTime lastLoginAt;

    public User() {
    }
}