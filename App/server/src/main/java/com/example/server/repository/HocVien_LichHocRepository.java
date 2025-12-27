package com.example.server.repository;

import com.example.server.entity.ClassSchedule;
import com.example.server.dto.HocVien_LichHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface HocVien_LichHocRepository extends JpaRepository<ClassSchedule, Integer> {
    @Query(value = """
        SELECT
            u.Username,
            s.StudentNumber,
            s.FullName,
            c.ClassCode,
            co.CourseCode,
            co.CourseName,
            cs.DayOfWeek,
            cs.StartTime,
            cs.EndTime,
            cs.Room
        FROM [User] u
        JOIN Student s ON s.StudentId = u.UserId 
        JOIN Registration r ON r.StudentId = s.StudentId
        JOIN Class c ON c.ClassId = r.ClassId
        JOIN Course co ON co.CourseId = c.CourseId
        JOIN ClassSchedule cs ON cs.ClassId = c.ClassId
        WHERE u.Username = :username
          AND r.Status = 'Registered'
        ORDER BY
            cs.DayOfWeek ASC,
            cs.StartTime ASC
    """, nativeQuery = true)
    List<HocVien_LichHoc> findScheduleByUsername(@Param("username") String username);
}