package com.example.server.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.repository.AnnouncementRepository;
import com.example.server.dto.AnnouncementDTO;
import com.example.server.entity.Announcement;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {
    
    @Autowired
    private AnnouncementRepository announcementRepository; // Inject Repository vào Service

    private AnnouncementDTO convertToDTO(Announcement announcement) {
        // Ánh xạ từ Entity sang DTO
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setAnnouncementId(announcement.getAnnouncementId());
        dto.setTitle(announcement.getTitle());
        dto.setBody(announcement.getBody());
        dto.setAuthorId(announcement.getAuthorId());
        dto.setIsGlobal(announcement.getIsGlobal());
        dto.setTargetClassId(announcement.getTargetClassId());
        dto.setCreatedAt(announcement.getCreatedAt());
        dto.setUpdatedAt(announcement.getUpdatedAt());
        return dto;
    }

    private Announcement convertToEntity(AnnouncementDTO announcementDTO) {
        // Ánh xạ từ DTO sang Entity
        Announcement announcement = new Announcement();
        announcement.setAnnouncementId(announcementDTO.getAnnouncementId());
        announcement.setTitle(announcementDTO.getTitle());
        announcement.setBody(announcementDTO.getBody());
        announcement.setAuthorId(announcementDTO.getAuthorId());
        announcement.setIsGlobal(announcementDTO.getIsGlobal());
        announcement.setTargetClassId(announcementDTO.getTargetClassId());
        announcement.setCreatedAt(announcementDTO.getCreatedAt());
        announcement.setUpdatedAt(announcementDTO.getUpdatedAt());
        return announcement;
    }

    @Override
    public List<AnnouncementDTO> findAllAnnouncements() {
        // 1. Dùng Repository lấy Entity List
        List<Announcement> announcements = announcementRepository.findAll();
        // 2. Chuyển List Entity sang List DTO
        return announcements.stream()
                    .map(this::convertToDTO)
                    .toList();
    }

    @Override
    public Optional<AnnouncementDTO> findAnnouncementById(Integer id) {
        // 1. Dùng Repository tìm Entity theo ID
        return announcementRepository.findById(id)
            // 2. Chuyển Optional<Entity> sang Optional<DTO>
            .map(this::convertToDTO);
    }

    @Override
    public AnnouncementDTO saveAnnouncement(AnnouncementDTO announcementDTO) {
        // 1. Chuyển DTO sang Entity
        Announcement announcementToSave = convertToEntity(announcementDTO);
        // 2. Lưu Entity (Spring Data JPA tự động INSERT nếu Id null/0 hoặc UPDATE nếu Id đã tồn tại)
        Announcement savedAnnouncement = announcementRepository.save(announcementToSave);
        // 3. Trả về DTO của Entity đã được lưu
        return convertToDTO(savedAnnouncement);
    }

    @Override
    public void deleteAnnouncement(Integer id) {
        announcementRepository.deleteById(id);
    }

}
