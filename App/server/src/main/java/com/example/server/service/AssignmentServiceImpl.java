package com.example.server.service;

import com.example.server.dto.AssignmentDTO;
import com.example.server.entity.Assignment;
import com.example.server.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Lớp triển khai các dịch vụ liên quan đến Bài tập.
 * Đã xử lý chuyển đổi dữ liệu giữa Entity (Database) và DTO (API).
 */
@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;

    // Định dạng ngày tháng chuẩn ISO (Ví dụ: 2024-12-24T15:00:00)
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Autowired
    public AssignmentServiceImpl(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    // --- CÁC HÀM CHUYỂN ĐỔI (MAPPING) ---

    private AssignmentDTO mapToDTO(Assignment assignment) {
        AssignmentDTO dto = new AssignmentDTO();
        dto.setAssignmentId(assignment.getAssignmentId());
        dto.setClassId(assignment.getClassId());
        dto.setTitle(assignment.getTitle());
        dto.setDescription(assignment.getDescription());

        // Chuyển đổi LocalDateTime -> String để gửi về Android
        if (assignment.getDueDate() != null) {
            dto.setDueDate(assignment.getDueDate().format(formatter));
        }

        if (assignment.getCreatedAt() != null) {
            dto.setCreatedAt(assignment.getCreatedAt().format(formatter));
        }

        return dto;
    }

    private Assignment mapToEntity(AssignmentDTO dto) {
        Assignment entity = new Assignment();
        entity.setAssignmentId(dto.getAssignmentId());
        entity.setClassId(dto.getClassId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());

        // Chuyển đổi String -> LocalDateTime để lưu vào SQL Server
        if (dto.getDueDate() != null && !dto.getDueDate().isEmpty()) {
            try {
                // Thử parse chuỗi từ Android gửi lên
                entity.setDueDate(LocalDateTime.parse(dto.getDueDate(), formatter));
            } catch (Exception e) {
                // Nếu Android gửi sai định dạng (ví dụ dd/MM/yyyy thay vì ISO)
                System.err.println("Lỗi định dạng ngày tháng từ Client: " + dto.getDueDate());
            }
        }

        return entity;
    }

    // --- TRIỂN KHAI CÁC PHƯƠNG THỨC INTERFACE ---

    @Override
    public List<AssignmentDTO> findAllAssignments() {
        return assignmentRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssignmentDTO> findAssignmentsByClassId(Integer classId) {
        return assignmentRepository.findByClassId(classId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AssignmentDTO> findAssignmentById(Integer id) {
        return assignmentRepository.findById(id)
                .map(this::mapToDTO);
    }

    @Override
    public AssignmentDTO saveAssignment(AssignmentDTO assignmentDTO) {
        Assignment assignment = mapToEntity(assignmentDTO);
        Assignment savedAssignment = assignmentRepository.save(assignment);
        return mapToDTO(savedAssignment);
    }

    @Override
    public void deleteAssignment(Integer id) {
        if (assignmentRepository.existsById(id)) {
            assignmentRepository.deleteById(id);
        }
    }
}