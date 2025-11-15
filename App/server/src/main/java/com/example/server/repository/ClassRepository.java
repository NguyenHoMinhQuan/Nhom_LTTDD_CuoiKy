package com.example.server.repository;

import org.springframework.stereotype.Repository;
import com.example.server.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ClassRepository extends JpaRepository<Class, Integer>  {
    
}
