package com.example.server.controller;

import com.example.server.dto.HocVien_SoYeuLiLich;
import com.example.server.service.HocVien_SoYeuLiLichService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/hocvien")
public class HocVien_SoYeuLiLichController {
    @Autowired
    private HocVien_SoYeuLiLichService liLichHocVien ;
    @GetMapping("/soyeulilich")
    public ResponseEntity<?> layThongTinHocVien(@RequestParam String Username){
        HocVien_SoYeuLiLich liLich = liLichHocVien.layThongTinHocVien(Username);

        if(liLich == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(liLich);
    }
}
//http://localhost:8080/api/hocvien/soyeulilich?Username=student1