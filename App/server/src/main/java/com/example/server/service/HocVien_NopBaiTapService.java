package com.example.server.service;

import com.example.server.dto.HocVien_NopBaiTap;
import com.example.server.repository.HocVien_NopBaiTapRepository;
import org.springframework.stereotype.Service;


@Service
public class HocVien_NopBaiTapService {
    private final HocVien_NopBaiTapRepository nopBaiTapRepository;


    public HocVien_NopBaiTapService(HocVien_NopBaiTapRepository hocVien_NopBaiTapRepository) {
        this.nopBaiTapRepository = hocVien_NopBaiTapRepository;
    }
    public boolean nopBaiTap(HocVien_NopBaiTap baiTap) {
        try {
            // Gọi hàm repository để chạy thủ tục SQL
            nopBaiTapRepository.NopBaiTap(
                    baiTap.getUsername(),
                    baiTap.getClassCode(),
                    baiTap.getAssignmentId(),
                    baiTap.getFileUrl()
            );
            return true; // Thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Thất bại
        }
    }
}
