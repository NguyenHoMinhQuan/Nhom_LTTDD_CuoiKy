package com.example.server.service;


import com.example.server.dto.HocVien_LichHoc;
import com.example.server.repository.HocVien_LichHocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HocVien_LichHocService {
    @Autowired
    private HocVien_LichHocRepository hocVien_lichHocRepository;
    public List<HocVien_LichHoc> LayLichHocTheoTenDN_HocVien(String Username){
        return hocVien_lichHocRepository.findScheduleByUsername(Username);
    }

}
