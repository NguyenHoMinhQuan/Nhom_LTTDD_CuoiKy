package com.example.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.server.entity.ClassMessage;

@Repository
public interface MessageRepository extends JpaRepository<ClassMessage, Integer> {
    List<ClassMessage> findByClassIdOrderBySentAtAsc(Integer classId);
}
