package com.example.server.service;

import com.example.server.dto.TinNhanDto;
import com.example.server.entity.TinNhan;
import com.example.server.repository.TinNhanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service // Đánh dấu đây là Service để Spring quản lý
public class TinNhanService {

    @Autowired
    private TinNhanRepository tinNhanRepo;

    // Hàm lấy lịch sử chat
    public List<TinNhanDto> layLichSuChat(Integer classId) {
        // Gọi thẳng Repository
        return tinNhanRepo.getChatHistory(classId);
    }

    // Hàm gửi tin nhắn
    public TinNhan guiTinNhan(TinNhan tinNhan) {
        // Xử lý logic thời gian tại đây
        tinNhan.setThoiGianGui(LocalDateTime.now());
        return tinNhanRepo.save(tinNhan);
    }
}