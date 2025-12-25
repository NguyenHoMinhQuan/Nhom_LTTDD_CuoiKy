package com.example.server.service;

import com.example.server.dto.LecturerDTO;
import com.example.server.dto.LecturerProfileDTO;
import com.example.server.entity.Lecturer;
import com.example.server.entity.User;
import com.example.server.repository.LecturerRepository;
import com.example.server.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LecturerServiceImpl implements LecturerService {

    private final LecturerRepository lecturerRepository;

    private final UserRepository userRepository;

    public LecturerServiceImpl(LecturerRepository lecturerRepository, UserRepository userRepository) {
        this.lecturerRepository = lecturerRepository;
        this.userRepository = userRepository;
    }

    // ===== convert =====
    private LecturerDTO convertToDTO(Lecturer l) {
        LecturerDTO dto = new LecturerDTO();
        dto.setLecturerId(l.getLecturerId());
        dto.setStaffNumber(l.getStaffNumber());
        dto.setFullName(l.getFullName());
        dto.setDepartment(l.getDepartment());
        return dto;
    }

    private Lecturer convertToEntity(LecturerDTO dto) {
        Lecturer l = new Lecturer();
        l.setLecturerId(dto.getLecturerId());
        l.setStaffNumber(dto.getStaffNumber());
        l.setFullName(dto.getFullName());
        l.setDepartment(dto.getDepartment());
        return l;
    }

    // ===== CRUD =====
    @Override
    public List<LecturerDTO> findAllLecturers() {
        return lecturerRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public Optional<LecturerDTO> findLecturerById(Integer id) {
        return lecturerRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public LecturerDTO createLecturer(LecturerDTO lecturerDTO) {
        Lecturer lecturer = convertToEntity(lecturerDTO);
        Lecturer saved = lecturerRepository.save(lecturer);
        return convertToDTO(saved);
    }

    @Override
    public Optional<LecturerDTO> updateLecturer(Integer id, LecturerDTO lecturerDTO) {
        return lecturerRepository.findById(id)
                .map(existing -> {
                    existing.setStaffNumber(lecturerDTO.getStaffNumber());
                    existing.setFullName(lecturerDTO.getFullName());
                    existing.setDepartment(lecturerDTO.getDepartment());
                    return convertToDTO(lecturerRepository.save(existing));
                });
    }

    @Override
    public void deleteLecturer(Integer id) {
        lecturerRepository.deleteById(id);
    }

    @Override
    public Optional<LecturerProfileDTO> getLecturerProfile(Integer id) {
        return lecturerRepository.findById(id).map(lecturer -> {
            LecturerProfileDTO dto = new LecturerProfileDTO();
            // Map thông tin giảng viên
            dto.setLecturerId(lecturer.getLecturerId());
            dto.setFullName(lecturer.getFullName());
            dto.setStaffNumber(lecturer.getStaffNumber());
            dto.setDepartment(lecturer.getDepartment());

            // Map thông tin tài khoản User đi kèm
            if (lecturer.getUser() != null) {
                dto.setUsername(lecturer.getUser().getUsername());
                dto.setEmail(lecturer.getUser().getEmail());
                // Không nên trả về password, hoặc để trống
            }
            return dto;
        });
    }

    @Override
    @Transactional // Quan trọng: Để update cả 2 bảng, nếu lỗi thì rollback hết
    public LecturerProfileDTO updateLecturerProfile(Integer id, LecturerProfileDTO dto) {
        Lecturer lecturer = lecturerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên với ID: " + id));

        // 1. Cập nhật bảng Lecturer
        lecturer.setFullName(dto.getFullName());
        lecturer.setDepartment(dto.getDepartment());
        lecturer.setStaffNumber(dto.getStaffNumber());

        // 2. Cập nhật bảng User liên kết
        User user = lecturer.getUser();
        if (user != null) {
            user.setEmail(dto.getEmail());
            
            // Logic đổi mật khẩu: Chỉ đổi nếu người dùng nhập pass mới
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                user.setPassword(dto.getPassword()); 
                // Lưu ý: Thực tế bạn nên mã hóa pass ở đây, ví dụ: passwordEncoder.encode(dto.getPassword())
            }
            userRepository.save(user); // Lưu User
        }

        Lecturer savedLecturer = lecturerRepository.save(lecturer); // Lưu Lecturer
        
        // Trả về DTO đã cập nhật
        dto.setLecturerId(savedLecturer.getLecturerId());
        return dto;
    }
}
