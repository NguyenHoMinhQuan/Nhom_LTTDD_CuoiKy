package com.example.server.repository;

import com.example.server.entity.Assignment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface HocVien_NopBaiTapRepository extends JpaRepository<Assignment, Integer> {
    @Modifying // Bắt buộc có vì đây là lệnh INSERT/UPDATE dữ liệu
    @Transactional // Bắt buộc có để quản lý giao dịch
    @Query(value = "EXEC dbo.usp_SubmitAssignment_Once :username, :classCode, :assignmentId, :fileUrl", nativeQuery = true)
    void NopBaiTap(String username, String classCode, Integer assignmentId, String fileUrl);
}


