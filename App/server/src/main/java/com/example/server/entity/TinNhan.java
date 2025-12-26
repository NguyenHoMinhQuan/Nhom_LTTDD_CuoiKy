package com.example.server.entity;

import com.fasterxml.jackson.annotation.JsonProperty; 
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "ClassMessage")
@Data
public class TinNhan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MessageId")
    private Integer id;

    @Column(name = "ClassId")
    @JsonProperty("classId") // Khớp với Client
    private Integer classId;

    @Column(name = "SenderId")
    @JsonProperty("senderId") //  Ánh xạ "senderId" (Client) vào "nguoiGuiId" (Server)
    private Integer nguoiGuiId; 

    @Column(name = "Content")
    @JsonProperty("content")  //  Ánh xạ "content" (Client) vào "noiDung" (Server)
    private String noiDung;

    @Column(name = "SentAt")
    private LocalDateTime thoiGianGui;
}