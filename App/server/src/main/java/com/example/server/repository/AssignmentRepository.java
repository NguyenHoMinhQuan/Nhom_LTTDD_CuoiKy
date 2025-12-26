package com.example.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.server.entity.Assignment;
import java.util.List;


@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    List<Assignment> findByClassId(Integer classId);
        @Query("SELECT a FROM Assignment a WHERE a.classId IN " +
        "(SELECT c.classId FROM Class c WHERE c.lecturerId = :lecturerId)")
    List<Assignment> findByLecturerId(@Param("lecturerId") Integer lecturerId);

    
}
