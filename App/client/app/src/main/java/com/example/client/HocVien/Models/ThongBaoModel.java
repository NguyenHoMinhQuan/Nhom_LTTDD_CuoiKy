package com.example.client.HocVien.Models;

import com.google.gson.annotations.SerializedName;

public class ThongBaoModel {

    @SerializedName("announcementId")
    private Integer id;

    @SerializedName("title")
    private String tieuDe;

    @SerializedName("body")
    private String noiDung;

    @SerializedName("createdAt")
    private String ngayTao;

    public ThongBaoModel() { }

    public String getTieuDe() { return tieuDe; }
    public String getNoiDung() { return noiDung; }
    public String getNgayTao() { return ngayTao; }
}