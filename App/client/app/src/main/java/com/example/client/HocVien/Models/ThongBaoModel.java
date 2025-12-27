package com.example.client.HocVien.Models;

import com.google.gson.annotations.SerializedName;

public class ThongBaoModel {

    // Server trả về "announcementId" -> Map vào biến "id"
    @SerializedName("announcementId")
    private Integer id;

    // Server trả về "title" -> Map vào biến "tieuDe"
    @SerializedName("title")
    private String tieuDe;

    // Server trả về "body" -> Map vào biến "noiDung"
    @SerializedName("body")
    private String noiDung;

    // Server trả về "createdAt" -> Map vào biến "ngayTao"
    @SerializedName("createdAt")
    private String ngayTao;

    // Constructor mặc định
    public ThongBaoModel() { }

    // --- Getter ---

    public Integer getId() {
        return id;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public String getNgayTao() {
        return ngayTao;
    }
}

// dứng dữ liệu đc đưa từ service lên