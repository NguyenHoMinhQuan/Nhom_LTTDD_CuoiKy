package com.example.server.service;

import com.example.server.dto.DanhGiaDto;
import com.example.server.entity.DanhGia;
import com.example.server.repository.DanhGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DanhGiaService {

    @Autowired
    private DanhGiaRepository danhGiaRepository;

    public DanhGia luuDanhGia(DanhGiaDto dto) {
        // 1. Tạo Entity mới từ dữ liệu DTO
        DanhGia danhGia = new DanhGia();
        
        danhGia.setClassId(dto.getClassId());
        danhGia.setSinhVienId(dto.getSinhVienId());
        danhGia.setSoSao(dto.getSoSao());
        danhGia.setNhanXet(dto.getNhanXet());
        
        // 2. Set thời gian hiện tại
        danhGia.setNgayTao(LocalDateTime.now());

        // 3. Lưu vào Database
        return danhGiaRepository.save(danhGia);
    }
}