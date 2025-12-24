package com.example.server.service;

import com.example.server.dto.HocVien_SoYeuLiLich;
import com.example.server.repository.HocVien_SoYeuLiLichReposity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HocVien_SoYeuLiLichService {
    @Autowired
    private HocVien_SoYeuLiLichReposity liLich ;
    public HocVien_SoYeuLiLich layThongTinHocVien(String Username){
        Optional<HocVien_SoYeuLiLich> duLieu = liLich.findProfileByUsername(Username);

        return duLieu.orElse(null);
    }
}
