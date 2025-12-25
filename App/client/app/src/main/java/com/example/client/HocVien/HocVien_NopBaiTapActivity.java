package com.example.client.HocVien;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// 1. SỬA IMPORT: Dùng XemDiemDto thay vì BaiTapDto
import com.example.client.HocVien.Models.HocVien_XemDiemDto;
import com.example.client.Models.HocVien_NopBaiDto;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HocVien_NopBaiTapActivity extends AppCompatActivity {

    private TextView tvSubjectName, tvDeadline, tvAssignmentContent , btnBack;
    private Button btnNewFile, btnSubmit;

    // 2. SỬA BIẾN TOÀN CỤC
    private HocVien_XemDiemDto duLieuBaiTap;
    private String linkFileDaChon = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hocvien_layout_assignment);

        anhXaView();

        // 3. SỬA PHẦN NHẬN DỮ LIỆU
        if (getIntent().hasExtra("DU_LIEU_BAI_TAP")) {
            // Ép kiểu về HocVien_XemDiemDto
            duLieuBaiTap = (HocVien_XemDiemDto) getIntent().getSerializableExtra("DU_LIEU_BAI_TAP");
            hienThiThongTin();
        } else {
            Toast.makeText(this, "Lỗi: Không có dữ liệu!", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnNewFile.setOnClickListener(v -> {
            linkFileDaChon = "https://drive.google.com/file-bai-lam-cua-toi.docx";
            btnNewFile.setText("bai-lam.docx (Đã chọn)");
            Toast.makeText(this, "Đã chọn file!", Toast.LENGTH_SHORT).show();
        });

        btnSubmit.setOnClickListener(v -> {
            if (linkFileDaChon.isEmpty()) {
                Toast.makeText(this, "Bạn chưa chọn file!", Toast.LENGTH_SHORT).show();
                return;
            }
            goiApiNopBai();
        });
        btnBack.setOnClickListener(v -> {
            finish(); // Quay về danh sách và kích hoạt onResume()
        });
    }

    private void anhXaView() {
        tvSubjectName = findViewById(R.id.tvSubjectName);
        tvDeadline = findViewById(R.id.tvDeadline);
        tvAssignmentContent = findViewById(R.id.tvAssignmentContent);
        btnNewFile = findViewById(R.id.btnNewFile);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);
        tvSubjectName = findViewById(R.id.tvSubjectName);
    }

    private void hienThiThongTin() {
        // Code hiển thị vẫn giữ nguyên, vì các hàm get... giống hệt nhau
        tvSubjectName.setText(duLieuBaiTap.getCourseName() + " - " + duLieuBaiTap.getClassCode());

        if (duLieuBaiTap.getDueDate() != null) {
            tvDeadline.setText("Hạn cuối: " + duLieuBaiTap.getDueDate().replace("T", " "));
        } else {
            tvDeadline.setText("Không có hạn nộp");
        }

        tvAssignmentContent.setText(duLieuBaiTap.getAssignmentTitle());

        String trangThai = duLieuBaiTap.getSubmitStatus();
        if (trangThai != null && trangThai.equalsIgnoreCase("Đã nộp")) {
            khoaGiaoDienDaNop();
        }
    }

    private void khoaGiaoDienDaNop() {
        btnSubmit.setEnabled(false);
        btnSubmit.setText("ĐÃ NỘP BÀI");
        btnSubmit.setBackgroundColor(Color.GRAY);
        btnNewFile.setEnabled(false);
        btnNewFile.setText("Đã có file trên hệ thống");

        if (duLieuBaiTap.getGrade() != null) {
            tvAssignmentContent.append("\n\n----------------\nKẾT QUẢ: " + duLieuBaiTap.getGrade() + " điểm");
        }
    }

    private void goiApiNopBai() {
        HocVien_NopBaiDto request = new HocVien_NopBaiDto(
                duLieuBaiTap.getUsername(),
                duLieuBaiTap.getClassCode(),
                duLieuBaiTap.getAssignmentId(),
                linkFileDaChon
        );

        // Lưu ý: Kiểm tra xem ApiClient.getClient() của bạn có cần tham số Context (this) hay không
        ApiService api = ApiClient.getClient(this).create(ApiService.class);
        api.nopBaiTap(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(HocVien_NopBaiTapActivity.this, "Nộp bài thành công!", Toast.LENGTH_LONG).show();
                    khoaGiaoDienDaNop();
                } else {
                    // --- SỬA ĐOẠN NÀY ĐỂ XEM LỖI ---
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Loi_Nop_Bai", "Mã lỗi: " + response.code() + " - Chi tiết: " + errorBody);
                        Toast.makeText(HocVien_NopBaiTapActivity.this, "Lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(HocVien_NopBaiTapActivity.this, "Lỗi kết nối server!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}