package com.example.server.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "Feedback") // Tên bảng trong SQL
@Data
public class DanhGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FeedbackId")
    private Integer id;

    @Column(name = "ClassId")
    private Integer classId;

    @Column(name = "StudentId")
    private Integer sinhVienId;

    @Column(name = "Rating")
    private Integer soSao; // 1-5

    @Column(name = "Comment")
    private String nhanXet;

    @Column(name = "CreatedAt")
    private LocalDateTime ngayTao;
}