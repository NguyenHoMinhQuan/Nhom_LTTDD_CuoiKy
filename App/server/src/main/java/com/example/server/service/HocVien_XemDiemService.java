package com.example.server.service;

import com.example.server.dto.HocVien_XemDiemDto;
import com.example.server.repository.HocVien_XemDiemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HocVien_XemDiemService {

    @Autowired
    private HocVien_XemDiemRepository hocVien_xemDiemRepository;

    // Sửa tên tham số thành chữ thường (camelCase) và dùng tiếng Anh cho đồng bộ
    public List<HocVien_XemDiemDto> LayLichHocTheoTenDN_HocVien(String username, String classCode){
        // Gọi xuống Repository
        return hocVien_xemDiemRepository.xemDiemHocVienTheoLop(username, classCode);
    }
}
//http://localhost:9000/api/hocvien/xemdiem?username=student1&classCode=CT101_B