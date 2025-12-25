package com.example.server.repository;

import org.springframework.stereotype.Repository;
import com.example.server.entity.Class;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ClassRepository extends JpaRepository<Class, Integer>  {
    List<Class> findAllByLecturerId(Integer lecturerId);
}
