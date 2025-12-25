package com.example.client.HocVien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.client.HocVien.Models.DanhGiaModel;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {

    // Biến dữ liệu
    private int idLop = 0;
    private String tenLop = "";

    // Khai báo View
    private ImageView btnBack;
    private TextView tvCourseName;
    private RatingBar ratingBar;
    private TextInputEditText etComment;
    private Button btnCancel, btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hocvien_feedback); // Đảm bảo tên layout đúng

        // 1. Nhận dữ liệu từ màn hình trước
        nhanDuLieuIntent();

        // 2. Ánh xạ View
        anhXaView();

        // 3. Hiển thị dữ liệu lên giao diện
        setupGiaoDien();

        // 4. Bắt sự kiện click
        xuLySuKien();
    }

    private void nhanDuLieuIntent() {
        Intent intent = getIntent();
        // Lấy ID Lớp (Mặc định là 0 nếu không tìm thấy)
        idLop = intent.getIntExtra("ID_LOP", 0);
        tenLop = intent.getStringExtra("TEN_LOP");

        // --- KIỂM TRA QUAN TRỌNG ---
        // Nếu idLop vẫn bằng 0 nghĩa là bên kia quên truyền hoặc truyền sai tên
        if (idLop == 0) {
            Toast.makeText(this, "LỖI: Không nhận được ID Lớp! Vui lòng kiểm tra lại Adapter.", Toast.LENGTH_LONG).show();
        }
    }

    private void anhXaView() {
        btnBack = findViewById(R.id.btnBack);
        tvCourseName = findViewById(R.id.tvCourseName);
        ratingBar = findViewById(R.id.ratingBar);
        etComment = findViewById(R.id.etComment);
        btnCancel = findViewById(R.id.btnCancel);
        btnSend = findViewById(R.id.btnSend);
    }

    private void setupGiaoDien() {
        if (tenLop != null && !tenLop.isEmpty()) {
            tvCourseName.setText(tenLop);
        } else {
            tvCourseName.setText("Đánh giá khóa học");
        }
    }

    private void xuLySuKien() {
        // Nút quay lại & Hủy bỏ
        View.OnClickListener closeAction = v -> finish();
        btnBack.setOnClickListener(closeAction);
        btnCancel.setOnClickListener(closeAction);

        // Nút Gửi đánh giá
        btnSend.setOnClickListener(v -> xuLyGuiDanhGia());
    }

    private void xuLyGuiDanhGia() {
        // 1. Kiểm tra ID Lớp hợp lệ chưa
        if (idLop == 0) {
            Toast.makeText(this, "Lỗi: Không xác định được lớp học để đánh giá.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Lấy số sao
        int soSao = (int) ratingBar.getRating();
        if (soSao == 0) {
            Toast.makeText(this, "Vui lòng chọn số sao đánh giá!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Lấy nội dung
        String noiDung = etComment.getText().toString().trim();
        if (noiDung.isEmpty()) {
            noiDung = "Không có nhận xét"; // Giá trị mặc định nếu rỗng
        }

        // 4. Tạo Model gửi đi
        // Lưu ý: ID Sinh viên đang để cứng là 1, sau này cần lấy từ SharedPreferences/Session
        DanhGiaModel model = new DanhGiaModel(idLop, 1, soSao, noiDung);

        // 5. Gọi API
        goiApiGuiDanhGia(model);
    }

    private void goiApiGuiDanhGia(DanhGiaModel model) {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        apiService.guiDanhGia(model).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(FeedbackActivity.this, "Gửi đánh giá thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng màn hình sau khi thành công
                } else {
                    // In ra mã lỗi để debug (vd: 400, 500)
                    Toast.makeText(FeedbackActivity.this, "Gửi thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(FeedbackActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}