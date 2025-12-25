package com.example.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.server.entity.ClassSchedule;

@Repository
public interface ClassScheduleRepository extends JpaRepository<ClassSchedule,Integer>{
    List<ClassSchedule> findAllByClassId(Integer classId);
    List<ClassSchedule> findAllByClassIdIn(List<Integer> classIds);
}
