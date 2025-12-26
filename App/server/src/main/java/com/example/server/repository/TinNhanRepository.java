package com.example.server.repository;

import com.example.server.dto.TinNhanDto;
import com.example.server.entity.TinNhan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TinNhanRepository extends JpaRepository<TinNhan, Integer> {
    
    // Query lấy tin nhắn + Tự động tìm tên người gửi (Join bảng Student/Lecturer/User)
    @Query(value = """
        SELECT 
            m.MessageId AS messageId,
            m.ClassId AS classId,
            m.SenderId AS senderId,
            m.Content AS content,
            m.SentAt AS sentAt,
            CASE 
                WHEN s.FullName IS NOT NULL THEN s.FullName
                WHEN l.FullName IS NOT NULL THEN l.FullName
                ELSE u.Username
            END AS senderName
        FROM ClassMessage m
        JOIN [User] u ON m.SenderId = u.UserId
        LEFT JOIN Student s ON u.UserId = s.StudentId
        LEFT JOIN Lecturer l ON u.UserId = l.LecturerId
        WHERE m.ClassId = :classId
        ORDER BY m.SentAt ASC
    """, nativeQuery = true)
    List<TinNhanDto> getChatHistory(Integer classId);
}
//http://localhost:8080/api/chat/lop/1