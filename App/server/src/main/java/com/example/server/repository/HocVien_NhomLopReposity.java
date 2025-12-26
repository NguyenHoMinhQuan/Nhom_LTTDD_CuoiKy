package com.example.server.repository;

import com.example.server.dto.HocVien_NhomLopDto;
import com.example.server.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HocVien_NhomLopReposity extends JpaRepository<Registration, Integer> {
    @Query(value = """
        SELECT
            c.ClassId AS classId,  
            u.UserId,
            u.Username,
            st.StudentNumber,
            st.FullName AS StudentName,
            r.RegistrationId,
            r.Status,
            r.RegisteredAt,
            c.ClassCode,
            c.Semester,
            co.CourseCode,
            co.CourseName,
            l.FullName AS LecturerName
        FROM dbo.[User] u
        JOIN dbo.Student st
            ON st.StudentId = u.UserId
        JOIN dbo.Registration r
            ON r.StudentId = st.StudentId
        JOIN dbo.Class c
            ON c.ClassId = r.ClassId
        JOIN dbo.Course co
            ON co.CourseId = c.CourseId
        LEFT JOIN dbo.Lecturer l
            ON l.LecturerId = c.LecturerId
        WHERE u.Username = :username
        ORDER BY c.Semester, c.ClassCode
        """, nativeQuery = true)
    List<HocVien_NhomLopDto> layDanhSachNhomLop(@Param("username") String username);
}