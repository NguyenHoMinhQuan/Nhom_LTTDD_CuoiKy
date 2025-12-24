package com.example.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.dto.AdminDTO;
import com.example.server.entity.User;
// Không cần import Class vì ta sẽ dùng đường dẫn đầy đủ trong Query

@Repository
public interface AdminRepository extends JpaRepository<User, Integer> {

    // =====================================================
    // 1. USER – HIỂN THỊ DANH SÁCH
    // =====================================================
    @Query("""
        SELECT new com.example.server.dto.AdminDTO$UserRow(
            u.userId,
            u.username, 
            COALESCE(s.fullName, l.fullName, u.username),
            u.email,
            COALESCE(s.faculty, l.department, 'N/A'),
            r.RoleName,  
            u.isActive
        )
        FROM User u
        LEFT JOIN u.role r
        LEFT JOIN Student s ON u.userId = s.studentId
        LEFT JOIN Lecturer l ON u.userId = l.lecturerId
        ORDER BY u.userId DESC
    """)
    List<AdminDTO.UserRow> getAdminUsers();

    // =====================================================
    // 2. KHÓA HỌC – HIỂN THỊ DANH SÁCH
    // =====================================================
    // SỬA:
    // 1. Course: Dùng c.CourseId (Viết Hoa theo Entity Course)
    // 2. Class: Dùng cl.courseId (Viết thường theo Entity Class)
    // 3. Entity Class: Dùng tên đầy đủ 'com.example.server.entity.Class' để tránh lỗi
    @Query("""
    SELECT new com.example.server.dto.AdminDTO$CourseRow(
        c.CourseId, 
        c.CourseCode, 
        c.CourseName, 
        c.Credit, 
        c.Description
    )
    FROM Course c
    ORDER BY c.CourseId DESC 
""")
List<AdminDTO.CourseRow> getAdminCourses();

    // =====================================================
    // 3. LỊCH HỌC – HIỂN THỊ DANH SÁCH
    // =====================================================
    // SỬA: Tương tự, dùng đúng tên biến Hoa/Thường của từng Entity
        @Query("""

        SELECT new com.example.server.dto.AdminDTO$ClassRow(

            cs.scheduleId,

            c.CourseCode,

            c.CourseName,

            cl.classCode,

            COALESCE(l.fullName, 'Chưa có GV'),

            cs.room,

            cs.dayOfWeek,

            cs.startTime,

            cs.endTime

        )
        FROM ClassSchedule cs
        JOIN com.example.server.entity.Class cl ON cs.classId = cl.classId
        JOIN Course c ON cl.courseId = c.CourseId
        LEFT JOIN Lecturer l ON cl.lecturerId = l.lecturerId
        ORDER BY cs.scheduleId DESC
    """)
    List<AdminDTO.ClassRow> getAdminClasses();
    // =====================================================
    // 4. METADATA
    // =====================================================
    @Query(value = "SELECT DISTINCT Department FROM Lecturer WHERE Department IS NOT NULL", nativeQuery = true)
    List<String> getAllDepartments();

    @Query(value = "SELECT CourseName FROM Course", nativeQuery = true)
    List<String> getAllCourseNames();

    // =====================================================
    // 5. CÁC HÀM NATIVE QUERY (Thêm/Sửa/Xóa)
    // =====================================================
    // Phần này giữ nguyên vì nó tương tác trực tiếp với Database (không quan tâm Entity)
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO [User] (Username, Email, Password, RoleId, IsActive, CreatedAt) VALUES (?1, ?2, ?3, ?4, ?5, GETDATE())", nativeQuery = true)
    void insertUser(String username, String email, String password, Integer roleId, boolean isActive);

    @Query(value = "SELECT TOP 1 UserId FROM [User] ORDER BY UserId DESC", nativeQuery = true)
    Integer getLastUserId();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Student (StudentId, FullName, Faculty, StudentNumber, Year) VALUES (?1, ?2, ?3, ?4, 2024)", nativeQuery = true)
    void insertStudent(Integer userId, String fullName, String faculty, String studentNumber);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Lecturer (LecturerId, FullName, Department, StaffNumber) VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
    void insertLecturer(Integer userId, String fullName, String department, String staffNumber);

    @Modifying
    @Transactional
    @Query(value = "UPDATE [User] SET Email = ?2, RoleId = ?3, IsActive = ?4 WHERE UserId = ?1", nativeQuery = true)
    void updateUser(Integer userId, String email, Integer roleId, boolean isActive);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Student SET FullName = ?2, Faculty = ?3 WHERE StudentId = ?1", nativeQuery = true)
    void updateStudent(Integer userId, String fullName, String faculty);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Lecturer SET FullName = ?2, Department = ?3 WHERE LecturerId = ?1", nativeQuery = true)
    void updateLecturer(Integer userId, String fullName, String department);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Student WHERE StudentId = ?1", nativeQuery = true)
    void deleteStudent(Integer userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Lecturer WHERE LecturerId = ?1", nativeQuery = true)
    void deleteLecturer(Integer userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM [User] WHERE UserId = ?1", nativeQuery = true)
    void deleteUser(Integer userId);
    //Course thêm/sửa/xóa sẽ được bổ sung sau
    // Thêm khóa học
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Course (CourseCode, CourseName, Credit, Description) VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
    void insertCourse(String maMon, String tenMon, Integer tinChi, String moTa);

    // Cập nhật khóa học
    @Modifying
    @Transactional
    @Query(value = "UPDATE Course SET CourseCode = ?2, CourseName = ?3, Credit = ?4, Description = ?5 WHERE CourseId = ?1", nativeQuery = true)
    void updateCourse(Integer id, String maMon, String tenMon, Integer tinChi, String moTa);

    // Xóa khóa học
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Course WHERE CourseId = ?1", nativeQuery = true)
    void deleteCourse(Integer id);
    //Class thêm/sửa/xóa sẽ được bổ sung sau
    // Thêm lớp học
    @Modifying
    @Transactional
    @Query(value = """
        -- B1: Cập nhật giảng viên cho lớp (nếu có thay đổi)
        UPDATE Class 
        SET LecturerId = (SELECT TOP 1 LecturerId FROM Lecturer WHERE FullName = ?2)
        WHERE ClassCode = ?1;

        -- B2: Thêm lịch học mới
        INSERT INTO ClassSchedule (ClassId, DayOfWeek, StartTime, EndTime, Room)
        SELECT 
            (SELECT TOP 1 ClassId FROM Class WHERE ClassCode = ?1), -- Tìm ClassId từ ClassCode
            ?3, -- DayOfWeek
            ?4, -- StartTime
            ?5, -- EndTime
            ?6  -- Room
    """, nativeQuery = true)
    void insertClass(String classCode, String lecturerName, Integer dayOfWeek, String startTime, String endTime, String room);
    // Cập nhật lớp học
    @Modifying
    @Transactional
    @Query(value = """
        -- 1. Cập nhật Giảng viên cho bảng Class (Dựa trên Mã lớp)
        UPDATE Class 
        SET LecturerId = (SELECT TOP 1 LecturerId FROM Lecturer WHERE FullName = :lecturerName)
        WHERE ClassCode = :classCode;

        -- 2. Cập nhật Lịch học cho bảng ClassSchedule
        UPDATE ClassSchedule 
        SET ClassId = (SELECT TOP 1 ClassId FROM Class WHERE ClassCode = :classCode),
            DayOfWeek = :dayOfWeek,
            StartTime = :startTime,
            EndTime = :endTime,
            Room = :room
        WHERE ScheduleId = :scheduleId
    """, nativeQuery = true)
    void updateClass(
        @Param("classCode") String classCode,
        @Param("lecturerName") String lecturerName,
        @Param("dayOfWeek") Integer dayOfWeek,
        @Param("startTime") String startTime,
        @Param("endTime") String endTime,
        @Param("room") String room,
        @Param("scheduleId") Integer scheduleId
    );
    // Xóa lớp học
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ClassSchedule WHERE ScheduleId = ?1", nativeQuery = true)
    void deleteClass(Integer scheduleId);
}