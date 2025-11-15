package com.example.server.repository;

import com.example.server.entity.Role; // Import Entity đã tạo ở bước trước
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    
}
