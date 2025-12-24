package com.example.server.service;

import com.example.server.dto.HocVien_NhomLopDto;
import com.example.server.repository.HocVien_NhomLopReposity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HocVien_NhomLopService {
    @Autowired
    private HocVien_NhomLopReposity NhomLop ;
    public List<HocVien_NhomLopDto> LayNhomLopTheoTenDN_HocVien(String Username){
        return NhomLop.layDanhSachNhomLop(Username);
    }
}
