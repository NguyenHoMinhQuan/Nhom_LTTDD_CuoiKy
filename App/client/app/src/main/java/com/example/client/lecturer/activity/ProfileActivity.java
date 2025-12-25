package com.example.client.lecturer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.client.Login.LoginActivity;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
import com.example.client.lecturer.model.LecturerProfileDTO;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private EditText etUsername, etFullName, etEmail, etPassword;
    private Button btnSave, btnLogout;
    private ApiService apiService;
    private int currentUserId;

    // Biến tạm để lưu giữ những thông tin không hiển thị lên màn hình
    private String savedDepartment;
    private String savedStaffNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_profile);

        // Ánh xạ View
        etUsername = findViewById(R.id.et_profile_username);
        etFullName = findViewById(R.id.et_profile_fullname);
        etEmail = findViewById(R.id.et_profile_email);
        etPassword = findViewById(R.id.et_profile_password);
        btnSave = findViewById(R.id.btn_save_profile);
        btnLogout = findViewById(R.id.btn_logout);

        // Lấy ID
        SharedPreferences prefs = getSharedPreferences("AUTH_PREFS", MODE_PRIVATE);
        currentUserId = prefs.getInt("USER_ID", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        apiService = ApiClient.getClient(this).create(ApiService.class);
        loadProfileData();

        btnSave.setOnClickListener(v -> saveProfileData());
        btnLogout.setOnClickListener(v -> performLogout());
    }

    private void loadProfileData() {
        apiService.getLecturerProfile(currentUserId).enqueue(new Callback<LecturerProfileDTO>() {
            @Override
            public void onResponse(Call<LecturerProfileDTO> call, Response<LecturerProfileDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LecturerProfileDTO dto = response.body();

                    // Hiển thị dữ liệu lên màn hình
                    etUsername.setText(dto.getUsername());
                    etFullName.setText(dto.getFullName());
                    etEmail.setText(dto.getEmail());
                    etPassword.setHint("Nhập mật khẩu mới (nếu muốn đổi)");

                    // 🟢 QUAN TRỌNG: Lưu lại Department và StaffNumber vào biến tạm
                    savedDepartment = dto.getDepartment();
                    savedStaffNumber = dto.getStaffNumber();

                    // Nếu muốn hiển thị tên Khoa lên log để kiểm tra
                    Log.d("PROFILE", "Khoa: " + savedDepartment + " - MSGV: " + savedStaffNumber);
                }
            }

            @Override
            public void onFailure(Call<LecturerProfileDTO> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProfileData() {
        LecturerProfileDTO dto = new LecturerProfileDTO();
        dto.setLecturerId(currentUserId);

        // Lấy dữ liệu người dùng nhập từ giao diện
        dto.setFullName(etFullName.getText().toString().trim());
        dto.setEmail(etEmail.getText().toString().trim());
        dto.setUsername(etUsername.getText().toString()); // Thường username backend không cho sửa, nhưng cứ gửi kèm

        // Xử lý mật khẩu
        String newPass = etPassword.getText().toString().trim();
        if (!newPass.isEmpty()) {
            dto.setPassword(newPass);
        } else {
            dto.setPassword(null); // Gửi null để backend biết không đổi pass
        }

        // 🟢 QUAN TRỌNG: Gán lại Department và StaffNumber cũ vào DTO
        // Nếu không có bước này, server sẽ nhận null và gây lỗi
        dto.setDepartment(savedDepartment);
        dto.setStaffNumber(savedStaffNumber);

        apiService.updateLecturerProfile(currentUserId, dto).enqueue(new Callback<LecturerProfileDTO>() {
            @Override
            public void onResponse(Call<LecturerProfileDTO> call, Response<LecturerProfileDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Lỗi không xác định";
                        Log.e("UPDATE_FAIL", "Code: " + response.code() + ", Body: " + errorBody);
                        Toast.makeText(ProfileActivity.this, "Thất bại: " + response.code(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LecturerProfileDTO> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performLogout() {
        getSharedPreferences("AUTH_PREFS", MODE_PRIVATE).edit().clear().apply();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}