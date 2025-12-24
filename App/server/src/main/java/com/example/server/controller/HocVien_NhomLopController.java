package com.example.server.controller;

import com.example.server.dto.HocVien_NhomLopDto;
import com.example.server.dto.HocVien_SoYeuLiLich;
import com.example.server.service.HocVien_NhomLopService;
import com.example.server.service.HocVien_SoYeuLiLichService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/hocvien")
public class HocVien_NhomLopController {
    @Autowired
    private HocVien_NhomLopService NhomLopHocVien ;
    @GetMapping("/nhomlop")
    public ResponseEntity<?> layNhomLopHocVien(@RequestParam String Username){
        List<HocVien_NhomLopDto> nhomLop = NhomLopHocVien.LayNhomLopTheoTenDN_HocVien(Username);

        if(nhomLop == null || nhomLop.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(nhomLop);
    }
}
//http://localhost:8080/api/hocvien/nhomlop?Username=student1
