package com.example.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.server.dto.AdminDTO;
import com.example.server.entity.User;

@Repository
public interface AdminRepository extends JpaRepository<User, Integer> {

    // Query 1: Lấy User (Dựa trên file User.java của bạn)
    @Query("SELECT new com.example.server.dto.AdminDTO$UserRow(" + 
           "u.userId, COALESCE(s.fullName, l.fullName, u.username), u.email, " +
           "COALESCE(s.faculty, l.department, 'N/A'), u.isActive) " +
           "FROM User u " +
           "LEFT JOIN Student s ON u.userId = s.studentId " +
           "LEFT JOIN Lecturer l ON u.userId = l.lecturerId")
    List<AdminDTO.UserRow> getAdminUsers();

    // Query 2: Lấy Khóa Học (Dựa trên file Course.java của bạn)
    // Lưu ý: co.CourseName và co.CourseId phải viết hoa chữ C
    @Query("SELECT new com.example.server.dto.AdminDTO$CourseRow(" +
           "co.CourseId, co.CourseName, l.fullName, " +
           "(SELECT COUNT(r) FROM Registration r WHERE r.classId IN " +
           "(SELECT cl_sub.classId FROM Class cl_sub WHERE cl_sub.courseId = co.CourseId))) " +
           "FROM Course co " +
           "LEFT JOIN Class cl ON co.CourseId = cl.courseId " +
           "LEFT JOIN Lecturer l ON cl.lecturerId = l.lecturerId " +
           "GROUP BY co.CourseId, co.CourseName, l.fullName")
    List<AdminDTO.CourseRow> getAdminCourses();

    // Query 3: Lấy Lịch Học (Kết hợp cả ClassSchedule, Class và Course)
    // Phải dùng co.CourseName (viết hoa C) và cl.classCode (viết thường c) cho đúng từng file
    @Query("SELECT new com.example.server.dto.AdminDTO$ClassRow(" +
           "co.CourseName, cl.classCode, cs.room, cs.startTime, cs.endTime) " +
           "FROM ClassSchedule cs, Class cl, Course co " + 
           "WHERE cs.classId = cl.classId " +   
           "AND cl.courseId = co.CourseId")
    List<AdminDTO.ClassRow> getAdminClasses();
}