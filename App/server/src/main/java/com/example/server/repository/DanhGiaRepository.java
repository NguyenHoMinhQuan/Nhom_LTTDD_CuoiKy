package com.example.server.repository;

import com.example.server.entity.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {
}