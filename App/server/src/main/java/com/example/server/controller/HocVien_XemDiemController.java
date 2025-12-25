package com.example.server.controller;

import com.example.server.dto.HocVien_XemDiemDto;
import com.example.server.service.HocVien_XemDiemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/xemdiem")
public class HocVien_XemDiemController {
    @Autowired
    private HocVien_XemDiemService xemDiem ;
    @GetMapping("/view")
    public ResponseEntity<?> xemLichHoc (@RequestParam String Username , @RequestParam String MaLop){
        List<HocVien_XemDiemDto> duLieu = xemDiem.LayLichHocTheoTenDN_HocVien(Username, MaLop);

        if(duLieu == null || duLieu.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(duLieu);
    }
}
//http://localhost:8080/api/xemdiem/view?Username=student1&MaLop=CT101_B