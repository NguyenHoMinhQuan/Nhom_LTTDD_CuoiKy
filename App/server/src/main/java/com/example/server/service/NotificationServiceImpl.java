package com.example.server.service;

import com.example.server.dto.NotificationDTO;
import com.example.server.entity.Notification;
import com.example.server.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification getNotificationById(Integer id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy notification ID: " + id));
    }

    @Override
    public Notification updateNotification(Integer id, NotificationDTO dto) {
        Notification n = getNotificationById(id);

        n.setIsRead(dto.getIsRead());
        // userId & announcementId thường KHÔNG cho sửa
        // createdAt cũng không nên sửa

        return notificationRepository.save(n);
    }

    @Override
    public void markAsRead(Integer id) {
        Notification n = getNotificationById(id);
        n.setIsRead(true);
        notificationRepository.save(n);
    }
}
