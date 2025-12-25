package com.example.server.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "ClassMessage") // Tên bảng trong SQL
@Data
public class TinNhan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MessageId")
    private Integer id;

    @Column(name = "ClassId")
    private Integer classId;

    @Column(name = "SenderId")
    private Integer nguoiGuiId; 

    @Column(name = "Content")
    private String noiDung;

    @Column(name = "SentAt")
    private LocalDateTime thoiGianGui;
}