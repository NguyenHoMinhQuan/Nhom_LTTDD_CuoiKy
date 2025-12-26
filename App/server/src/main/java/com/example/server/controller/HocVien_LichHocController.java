package com.example.server.controller;

import com.example.server.service.HocVien_LichHocService;
import com.example.server.dto.HocVien_LichHoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/lichhoc")
public class HocVien_LichHocController {
    @Autowired
    private HocVien_LichHocService lichHoc ;
    @GetMapping("/view")
    public ResponseEntity<?> xemLichHoc (@RequestParam String Username){
        List<HocVien_LichHoc> duLieu = lichHoc.LayLichHocTheoTenDN_HocVien(Username);

        if(duLieu == null || duLieu.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(duLieu);
    }
}
//http://localhost:8080/api/lichhoc/view?Username=student1