package com.example.client.HocVien.Models;
import com.google.gson.annotations.SerializedName;

public class TinNhanModel {
    @SerializedName("classId") private int classId;
    @SerializedName("nguoiGuiId") private int nguoiGuiId;
    @SerializedName("noiDung") private String noiDung;

    public TinNhanModel(int classId, int nguoiGuiId, String noiDung) {
        this.classId = classId;
        this.nguoiGuiId = nguoiGuiId;
        this.noiDung = noiDung;
    }
    public int getNguoiGuiId() { return nguoiGuiId; }
    public String getNoiDung() { return noiDung; }
}