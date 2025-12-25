package com.example.client.HocVien.Models;

import com.google.gson.annotations.SerializedName;

public class ThongBaoModel {

    // Server trả về "announcementId" -> Mình hứng vào "id"
    @SerializedName("announcementId")
    private Integer id;//////

    // Server trả về "title" -> Mình hứng vào "tieuDe"
    @SerializedName("title")
    private String tieuDe;

    // Server trả về "body" -> Mình hứng vào "noiDung"
    @SerializedName("body")
    private String noiDung;

    // Server trả về "createdAt" -> Mình hứng vào "ngayTao"
    @SerializedName("createdAt")
    private String ngayTao;

    public ThongBaoModel() { }

    // Getter tiếng Việt
    public String getTieuDe() { return tieuDe; }
    public String getNoiDung() { return noiDung; }
    public String getNgayTao() { return ngayTao; }
}