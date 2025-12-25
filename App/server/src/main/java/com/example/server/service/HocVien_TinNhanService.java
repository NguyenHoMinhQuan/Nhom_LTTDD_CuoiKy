package com.example.server.service;

import com.example.server.dto.HocVien_TinNhanDto;
import com.example.server.repository.HocVien_TinNhanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HocVien_TinNhanService {

    @Autowired
    private HocVien_TinNhanRepository tinNhanRepository;

    public List<HocVien_TinNhanDto> getMessagesByClass(String username, String classCode) {
        // Gọi Repository
        return tinNhanRepository.layDanhSachTinNhan(username, classCode);
    }
}
