package com.example.client.HocVien.Models;
import com.google.gson.annotations.SerializedName;

public class DanhGiaModel {

    @SerializedName("classId")
    private int classId;

    @SerializedName("studentId")
    private int sinhVienId;

    @SerializedName("rating")
    private int soSao;

    @SerializedName("comment")
    private String nhanXet;

    public DanhGiaModel(int classId, int sinhVienId, int soSao, String nhanXet) {
        this.classId = classId;
        this.sinhVienId = sinhVienId;
        this.soSao = soSao;
        this.nhanXet = nhanXet;
    }
}