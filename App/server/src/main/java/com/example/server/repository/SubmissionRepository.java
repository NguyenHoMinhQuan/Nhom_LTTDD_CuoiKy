package com.example.server.repository;

import com.example.server.dto.SubmissionDTO;
import com.example.server.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Integer> {
    List<Submission> findByAssignmentId(Integer assignmentId);
    @Query("SELECT new com.example.server.dto.SubmissionDTO(" +
            "s.submissionId, s.assignmentId, s.studentId, st.fullName, s.fileUrl, s.submittedAt, s.grade) " +
            "FROM Submission s JOIN Student st ON s.studentId = st.studentId " +
            "WHERE s.assignmentId = :assignmentId")
    List<SubmissionDTO> findSubmissionsWithStudentName(@Param("assignmentId") Integer assignmentId);

}
