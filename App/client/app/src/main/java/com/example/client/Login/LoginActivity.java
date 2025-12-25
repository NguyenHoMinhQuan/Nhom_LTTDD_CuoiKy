package com.example.client.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.client.AdminDashboardActivity;
import com.example.client.HocVien.HomeActivity;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
import com.example.client.Login.LoginRequest;
import com.example.client.Login.LoginResponse;
import com.example.client.lecturer.activity.LecturerDashboardActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText edt_username, edt_pass;
    TextView txtv_lostpass;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🟢 KIỂM TRA TOKEN ĐỂ TỰ ĐỘNG ĐĂNG NHẬP (NẾU BẠN MUỐN)
        SharedPreferences prefs = getSharedPreferences("AUTH_PREFS", MODE_PRIVATE);
        String savedToken = prefs.getString("JWT_TOKEN", null);
        String savedRole = prefs.getString("USER_ROLE", null);
        if (savedToken != null && savedRole != null) {
            navigateToRoleBasedScreen(savedRole);
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.public_login);

        edt_username = findViewById(R.id.edt_username);
        edt_pass = findViewById(R.id.edt_pass);
        txtv_lostpass = findViewById(R.id.tv_lostpass);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(v -> {

            String username = edt_username.getText().toString().trim();
            String password = edt_pass.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Nhập đủ tài khoản & mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }
            LoginRequest request = new LoginRequest(username, password);

            ApiService apiService = ApiClient
                    .getClient(getApplicationContext())
                    .create(ApiService.class);

            apiService.login(request).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call,
                                       Response<LoginResponse> response) {

                    if (response.isSuccessful()
                            && response.body() != null
                            && response.body().isSuccess()) {

                        LoginResponse data = response.body();

                        // 🔐 LƯU TOKEN, USERNAME, USER_ID VÀ ROLE
                        SharedPreferences.Editor editor = getSharedPreferences("AUTH_PREFS", MODE_PRIVATE).edit();
                        editor.putString("JWT_TOKEN", data.getToken());
                        editor.putString("USERNAME" , username);

                        // Sửa tại đây: Lưu thêm thông tin định danh
                        if (data.getUserProfile() != null) {
                            editor.putInt("USER_ID", data.getUserProfile().getUserId());
                            editor.putString("USER_ROLE", data.getUserProfile().getRole());
                        }
                        editor.apply();

                        String role = data.getUserProfile().getRole();
                        navigateToRoleBasedScreen(role);

                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Sai tài khoản hoặc mật khẩu",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this,
                            "Không kết nối được server",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // Hàm phụ để điều hướng, tránh lặp lại code
    private void navigateToRoleBasedScreen(String role) {
        Intent intent;
        if ("ROLE_ADMIN".equals(role)) {
            intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
        } else if ("ROLE_LECTURER".equals(role)) {
            intent = new Intent(LoginActivity.this, LecturerDashboardActivity.class);
        } else {
            intent = new Intent(LoginActivity.this, HomeActivity.class);
        }
        startActivity(intent);
        finish();
    }
}