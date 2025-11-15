package com.example.server.repository;

import com.example.server.entity.Announcement; // Import Entity đã tạo ở bước trước
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
    
}
