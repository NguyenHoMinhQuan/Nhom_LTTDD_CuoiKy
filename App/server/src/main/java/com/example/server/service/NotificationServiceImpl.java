package com.example.server.service;

import com.example.server.dto.NotificationDTO;
import com.example.server.entity.Notification;
import com.example.server.repository.AnnouncementRepository;
import com.example.server.repository.NotificationRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private AnnouncementRepository announcementRepository;

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
    public List<NotificationDTO> getUnreadNotificationsByUserId(Integer userId) {
        // 1. Tìm Notification chưa đọc (isRead = 0)
        List<Notification> unreadNotifications = notificationRepository
                .findByUserIdAndIsReadOrderByCreatedAtDesc(userId, 0); 

        // 2. Chuyển đổi sang DTO dựa trên đúng các trường bạn đã định nghĩa
        return unreadNotifications.stream().map(notification -> {
            NotificationDTO dto = new NotificationDTO();
            
            // Chỉ set những trường ĐANG CÓ trong NotificationDTO của bạn:
            dto.setNotificationId(notification.getNotificationId());
            dto.setIsRead(notification.getIsRead());
            dto.setCreatedAt(notification.getCreatedAt());

            // 3. Lấy Title và Body từ bảng Announcement (Join thông qua ManyToOne)
            // Lưu ý: Dùng đúng Getter Title/Body viết hoa của bạn trong Entity Announcement
            if (notification.getAnnouncement() != null) {
                dto.setTitle(notification.getAnnouncement().getTitle()); 
                dto.setBody(notification.getAnnouncement().getBody());
            } else {
                // Dự phòng nếu Hibernate chưa load kịp Announcement
                announcementRepository.findById(notification.getAnnouncementId()).ifPresent(ann -> {
                    dto.setTitle(ann.getTitle());
                    dto.setBody(ann.getBody());
                });
            }
            
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getAllNotificationsByUserId(Integer userId) {
        // Lấy tất cả thông báo của User, không quan tâm đã đọc hay chưa
        // Sắp xếp mới nhất lên đầu
        List<Notification> allNotifications = notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId); 

        return allNotifications.stream().map(notification -> {
            NotificationDTO dto = new NotificationDTO();
            dto.setNotificationId(notification.getNotificationId());
            dto.setCreatedAt(notification.getCreatedAt());
            dto.setIsRead(notification.getIsRead());

            // Lấy nội dung từ Announcement
            if (notification.getAnnouncement() != null) {
                dto.setTitle(notification.getAnnouncement().getTitle());
                dto.setBody(notification.getAnnouncement().getBody());
            } else {
                announcementRepository.findById(notification.getAnnouncementId()).ifPresent(ann -> {
                    dto.setTitle(ann.getTitle());
                    dto.setBody(ann.getBody());
                });
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(Integer notificationId) {
        // Cập nhật IsRead thành true (1)
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setIsRead(1);
            notificationRepository.save(n);
        });
    }
}
