package com.example.server.repository;

import com.example.server.dto.HocVien_TinNhanDto;
import com.example.server.entity.ClassMessage; // Hoặc entity chính của bảng tin nhắn
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TinNhanTheoMaRepository extends JpaRepository<ClassMessage, Integer> {

    @Query(value = """
        SELECT
            cm.MessageId,
            cm.ClassId,
            c.ClassCode,
            cm.SenderId,
            su.Username AS SenderUsername,
            COALESCE(st.FullName, le.FullName, su.Username) AS SenderName,
            cm.Content,
            cm.SentAt
        FROM dbo.[User] u
        JOIN dbo.Student stMe
            ON stMe.StudentId = u.UserId
        JOIN dbo.Registration r
            ON r.StudentId = stMe.StudentId
           AND r.Status <> N'Dropped' -- Loại bỏ lớp đã hủy
        JOIN dbo.Class c
            ON c.ClassId = r.ClassId
        JOIN dbo.ClassMessage cm
            ON cm.ClassId = c.ClassId
        JOIN dbo.[User] su
            ON su.UserId = cm.SenderId
        LEFT JOIN dbo.Student st
            ON st.StudentId = su.UserId
        LEFT JOIN dbo.Lecturer le
            ON le.LecturerId = su.UserId
        WHERE u.Username = :username
          AND c.ClassCode = :classCode
        ORDER BY cm.SentAt ASC, cm.MessageId ASC
        """, nativeQuery = true)
    List<HocVien_TinNhanDto> layDanhSachTinNhan(
            @Param("username") String username,
            @Param("classCode") String classCode
    );
}