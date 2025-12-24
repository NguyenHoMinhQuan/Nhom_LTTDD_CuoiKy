package com.example.server.repository;

import com.example.server.entity.Notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserIdAndIsReadOrderByCreatedAtDesc(Integer userId, Integer isRead);
    List<Notification> findByUserIdOrderByCreatedAtDesc(Integer userId);
}
