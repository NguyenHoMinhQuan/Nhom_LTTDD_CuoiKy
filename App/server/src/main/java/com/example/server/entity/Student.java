package com.example.server.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
@Entity
@Table(name ="Student")
@Data
public class Student {
    @Id
    @Column(name = "StudentId")
    private Integer studentId; // liên kết với UserId

    @Column(name = "StudentNumber", unique = true)
    private String studentNumber;

    @Column(name = "FullName")
    private String fullName;

    @Column(name = "DateOfBirth")
    private LocalDate dateOfBirth;

    @Column(name = "Faculty")
    private String faculty;

    @Column(name = "Year")
    private Integer year;

    @OneToOne
    @MapsId
    @JoinColumn(name = "StudentId")
    private User user;
}
