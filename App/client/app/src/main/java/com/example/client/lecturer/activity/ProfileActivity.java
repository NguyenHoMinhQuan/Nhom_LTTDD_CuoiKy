package com.example.client.lecturer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.client.Login.LoginActivity;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
import com.example.client.lecturer.model.LecturerProfileDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private EditText etUsername, etFullName, etEmail, etPassword;
    private Button btnSave, btnLogout;
    private ApiService apiService;
    private int currentUserId; // Lấy từ Intent hoặc SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_profile);

        // 1. Ánh xạ View
        etUsername = findViewById(R.id.et_profile_username);
        etFullName = findViewById(R.id.et_profile_fullname);
        etEmail = findViewById(R.id.et_profile_email);
        etPassword = findViewById(R.id.et_profile_password);
        btnSave = findViewById(R.id.btn_save_profile);
        btnLogout = findViewById(R.id.btn_logout);

        // 2. Lấy ID user (Giả sử bạn truyền qua Intent hoặc lấy từ Prefs)
        // Ví dụ lấy từ SharedPreferences nếu bạn đã lưu lúc Login
        SharedPreferences prefs = getSharedPreferences("AUTH_PREFS", MODE_PRIVATE);
        currentUserId = prefs.getInt("USER_ID", 2); // Mặc định là 2 để test

        // 3. Khởi tạo API
        apiService = ApiClient.getClient(this).create(ApiService.class);

        // 4. Load dữ liệu
        loadProfileData();

        // 5. Sự kiện Lưu
        btnSave.setOnClickListener(v -> saveProfileData());

        // 6. Sự kiện Đăng xuất
        btnLogout.setOnClickListener(v -> performLogout());
    }

    private void loadProfileData() {
        apiService.getLecturerProfile(currentUserId).enqueue(new Callback<LecturerProfileDTO>() {
            @Override
            public void onResponse(Call<LecturerProfileDTO> call, Response<LecturerProfileDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LecturerProfileDTO dto = response.body();
                    etUsername.setText(dto.getUsername());
                    etFullName.setText(dto.getFullName());
                    etEmail.setText(dto.getEmail());
                    etPassword.setText(dto.getPassword());
                } else {
                    Toast.makeText(ProfileActivity.this, "Lỗi tải thông tin", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LecturerProfileDTO> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProfileData() {
        LecturerProfileDTO dto = new LecturerProfileDTO();
        dto.setLecturerId(currentUserId);
        dto.setFullName(etFullName.getText().toString().trim());
        dto.setEmail(etEmail.getText().toString().trim());
        dto.setPassword(etPassword.getText().toString().trim());
        // Username thường không cho đổi nên không cần set hoặc set nguyên giá trị cũ

        apiService.updateLecturerProfile(currentUserId, dto).enqueue(new Callback<LecturerProfileDTO>() {
            @Override
            public void onResponse(Call<LecturerProfileDTO> call, Response<LecturerProfileDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay về Dashboard
                } else {
                    Toast.makeText(ProfileActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LecturerProfileDTO> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performLogout() {
        // Xóa Token và thông tin lưu trữ
        getSharedPreferences("AUTH_PREFS", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        // Chuyển về màn hình Login
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}