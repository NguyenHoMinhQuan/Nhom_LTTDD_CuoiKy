package com.example.server.entity;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
@Table(name = "Lecturer")
public class Lecturer {
    @Id
    @Column(name = "LecturerId")
    private Integer lecturerId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "LecturerId")
    private User user; // FK → bảng User(UserId)

    @Column(name = "StaffNumber", unique = true)
    private String staffNumber;

    @Column(name = "FullName")
    private String fullName;

    @Column(name = "Department")
    private String department;
}
