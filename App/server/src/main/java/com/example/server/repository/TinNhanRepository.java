package com.example.server.repository;

import com.example.server.entity.TinNhan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TinNhanRepository extends JpaRepository<TinNhan, Integer> {
    // Lấy tin nhắn theo lớp, tin cũ hiện trước
    List<TinNhan> findByClassIdOrderByThoiGianGuiAsc(Integer classId);
}