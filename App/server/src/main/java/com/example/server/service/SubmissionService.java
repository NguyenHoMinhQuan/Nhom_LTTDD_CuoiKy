package com.example.server.service;

import com.example.server.dto.SubmissionDTO;

import java.util.List;

public interface SubmissionService {
    List<SubmissionDTO> getSubmissionsByAssignment(Integer assignmentId);

    SubmissionDTO gradeSubmission(Integer submissionId, Double grade);

    long countSubmissions(Integer assignmentId);
}
