package com.example.server.service;

import com.example.server.dto.AnnouncementDTO;
import java.util.List;
import java.util.Optional;

public interface AnnouncementService {
    
    // Phương thức đọc: Lấy tất cả Announcement
    List<AnnouncementDTO> findAllAnnouncements();

    // Phương thức đọc: Lấy Announcement theo ID
    Optional<AnnouncementDTO> findAnnouncementById(Integer id);

    // Phương thức ghi: Tạo mới hoặc cập nhật Announcement
    AnnouncementDTO saveAnnouncement(AnnouncementDTO announcementDTO);
    
    // Phương thức xóa
    void deleteAnnouncement(Integer id);

}
