package com.example.client.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.client.AdminDashboardActivity;
import com.example.client.ForgotPassActivity;
import com.example.client.HocVien.HomeActivity;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
import com.example.client.lecturer.LecturerDashboardActivity;

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

                        // 🔐 LƯU TOKEN
                        getSharedPreferences("AUTH_PREFS", MODE_PRIVATE)
                                .edit()
                                .putString("JWT_TOKEN", data.getToken())
                                .putString("USERNAME" , username) // Thuận - giữ lại giá trị student1 để lọc dữ liệu trong trang học viên
                                .apply();

                        String role = data.getUserProfile().getRole();

                        if ("ROLE_ADMIN".equals(role)) {
                            startActivity(new Intent(LoginActivity.this,
                                    AdminDashboardActivity.class));
                        } else if ("ROLE_LECTURER".equals(role)) {
                            startActivity(new Intent(LoginActivity.this,
                                    LecturerDashboardActivity.class));
                        } else {
                            startActivity(new Intent(LoginActivity.this,
                                    HomeActivity.class));
                        }

                        finish(); // đóng login

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
    }}