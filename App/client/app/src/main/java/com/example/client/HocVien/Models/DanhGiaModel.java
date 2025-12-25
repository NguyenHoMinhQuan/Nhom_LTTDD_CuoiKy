package com.example.client.HocVien.Models;
import com.google.gson.annotations.SerializedName;

public class DanhGiaModel {
    @SerializedName("classId") private int classId;
    @SerializedName("sinhVienId") private int sinhVienId;
    @SerializedName("soSao") private int soSao;
    @SerializedName("nhanXet") private String nhanXet;

    public DanhGiaModel(int classId, int sinhVienId, int soSao, String nhanXet) {
        this.classId = classId;
        this.sinhVienId = sinhVienId;
        this.soSao = soSao;
        this.nhanXet = nhanXet;
    }
}