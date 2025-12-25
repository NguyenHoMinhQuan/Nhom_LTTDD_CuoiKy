package com.example.server.repository;

import com.example.server.dto.HocVien_SoYeuLiLich;
import com.example.server.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HocVien_SoYeuLiLichReposity extends JpaRepository<Student , Integer> {
    Student findByStudentNumber(String Username);
    @Query(value = """
        SELECT
            u.UserId,
            u.Username,
            u.Email,
            u.IsActive,
            u.CreatedAt,
            s.StudentNumber,
            s.FullName,
            s.DateOfBirth,
            s.Faculty,
            s.Year
        FROM [User] u
        JOIN Student s ON s.StudentId = u.UserId
        WHERE u.Username = :Username
    """, nativeQuery = true)
    Optional<HocVien_SoYeuLiLich> findProfileByUsername(@Param("Username") String username);
}
