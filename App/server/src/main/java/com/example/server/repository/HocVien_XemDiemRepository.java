package com.example.server.repository;

import com.example.server.dto.HocVien_LichHoc;
import com.example.server.dto.HocVien_XemDiemDto;
import com.example.server.entity.Assignment;
import com.example.server.entity.ClassSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HocVien_XemDiemRepository extends JpaRepository<Assignment, Integer> {
    @Query(value = """
        SELECT
            u.Username,
            st.StudentNumber,
            st.FullName,
            c.ClassCode,
            c.Semester,
            co.CourseCode,
            co.CourseName,
            a.AssignmentId,
            a.Title       AS AssignmentTitle,
            a.DueDate,
            s.SubmittedAt,
            s.Grade,
            CASE WHEN s.SubmissionId IS NULL THEN N'Chưa nộp' ELSE N'Đã nộp' END AS SubmitStatus
        FROM dbo.[User] u
        JOIN dbo.Student st
            ON st.StudentId = u.UserId
        JOIN dbo.Registration r
            ON r.StudentId = st.StudentId
           AND r.Status <> N'Dropped'
        JOIN dbo.Class c
            ON c.ClassId = r.ClassId
        JOIN dbo.Course co
            ON co.CourseId = c.CourseId
        JOIN dbo.Assignment a
            ON a.ClassId = c.ClassId
        LEFT JOIN dbo.Submission s
            ON s.AssignmentId = a.AssignmentId
           AND s.StudentId    = st.StudentId
        WHERE u.Username = :username
          AND c.ClassCode = :classCode
        ORDER BY a.DueDate, a.AssignmentId;    
            """, nativeQuery = true)
    List<HocVien_XemDiemDto> xemDiemHocVienTheoLop(
            @Param("username") String username,
            @Param("classCode") String classCode
    );
}
