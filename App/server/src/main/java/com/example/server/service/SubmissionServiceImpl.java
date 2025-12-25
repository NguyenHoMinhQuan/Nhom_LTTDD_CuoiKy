package com.example.server.service;

import com.example.server.dto.SubmissionDTO;
import com.example.server.entity.Submission;
import com.example.server.repository.SubmissionRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public SubmissionServiceImpl(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    @Override
    public List<SubmissionDTO> getSubmissionsByAssignment(Integer assignmentId) {
        return submissionRepository.findByAssignmentId(assignmentId)
                .stream()
                .map(sub -> {
                    SubmissionDTO dto = new SubmissionDTO();
                    dto.setSubmissionId(sub.getSubmissionId());
                    dto.setAssignmentId(sub.getAssignmentId());
                    dto.setStudentId(sub.getStudentId());
                    dto.setFileUrl(sub.getFileUrl());
                    dto.setGrade(sub.getGrade());
                    dto.setSubmittedAt(sub.getSubmittedAt() != null ? sub.getSubmittedAt().format(formatter) : null);
                    return dto;
                })
                .toList();
    }

    @Override
    public SubmissionDTO gradeSubmission(Integer submissionId, Double grade) {
        Submission sub = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy submission"));
        sub.setGrade(grade);
        submissionRepository.save(sub);

        SubmissionDTO dto = new SubmissionDTO();
        dto.setSubmissionId(sub.getSubmissionId());
        dto.setGrade(sub.getGrade());
        return dto;
    }

    @Override
    public long countSubmissions(Integer assignmentId) {
        return submissionRepository.findByAssignmentId(assignmentId).size();
    }
}
