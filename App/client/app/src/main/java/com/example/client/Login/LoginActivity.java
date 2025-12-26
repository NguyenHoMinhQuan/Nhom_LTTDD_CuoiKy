package com.example.client.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // Import thêm cái này để xem log
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.client.Admin.AdminDashboardActivity;
import com.example.client.HocVien.HomeActivity;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.public_login);

        // Ánh xạ View
        edt_username = findViewById(R.id.edt_username);
        edt_pass = findViewById(R.id.edt_pass);
        txtv_lostpass = findViewById(R.id.tv_lostpass);
        btn_login = findViewById(R.id.btn_login);

        // SỰ KIỆN CLICK (BẮT ĐẦU TRY-CATCH TỪ ĐÂY)
        btn_login.setOnClickListener(v -> {
            try {
                // 1. Lấy dữ liệu nhập vào
                String username = edt_username.getText().toString().trim();
                String password = edt_pass.getText().toString().trim();

                // 2. Kiểm tra rỗng
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đủ tài khoản & mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 3. Tạo Request
                LoginRequest request = new LoginRequest(username, password);

                // 4. Khởi tạo API Service (Có thể gây lỗi nếu Context null)
                ApiService apiService = ApiClient
                        .getClient(LoginActivity.this) // Dùng LoginActivity.this an toàn hơn getApplicationContext()
                        .create(ApiService.class);

                // Toast báo hiệu đã bắt đầu gọi (để biết code có chạy ko)
                Toast.makeText(LoginActivity.this, "Đang đăng nhập...", Toast.LENGTH_SHORT).show();

                // 5. Gọi API
                apiService.login(request).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        try {
                            // --- TRY-CATCH CHO PHẦN XỬ LÝ DỮ LIỆU VỀ ---
                            if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                                LoginResponse data = response.body();

                                // Kiểm tra null trước khi truy cập sâu
                                if(data.getUserProfile() == null){
                                    Toast.makeText(LoginActivity.this, "Lỗi: Không lấy được thông tin người dùng", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // 🔐 LƯU TOKEN
                                getSharedPreferences("AUTH_PREFS", MODE_PRIVATE)
                                        .edit()
                                        .putString("JWT_TOKEN", data.getToken())
                                        .putString("USERNAME", username)
                                        .apply();

                                String role = data.getUserProfile().getRole();

                                // Kiểm tra Role null
                                if (role == null) {
                                    Toast.makeText(LoginActivity.this, "Tài khoản chưa được phân quyền", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Điều hướng
                                if ("ROLE_ADMIN".equals(role)) {
                                    startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                                } else if ("ROLE_LECTURER".equals(role)) {
                                    startActivity(new Intent(LoginActivity.this, LecturerDashboardActivity.class));
                                } else {
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                }

                                finish(); // Đóng màn hình login

                            } else {
                                // Đăng nhập thất bại (Sai pass hoặc user không tồn tại)
                                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            // Bắt lỗi logic trong onResponse (ví dụ NullPointer khi getRole)
                            Log.e("Loi_Login_Response", "Lỗi xử lý dữ liệu: " + e.getMessage());
                            Toast.makeText(LoginActivity.this, "Lỗi dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        // Lỗi kết nối mạng hoặc Server chết
                        Log.e("Loi_Ket_Noi", "Lỗi mạng: " + t.getMessage());
                        Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                });

            } catch (Exception e) {
                // BẮT LỖI TỔNG QUÁT (Ví dụ lỗi khởi tạo ApiClient hoặc lỗi View)
                Log.e("Loi_Chung", "Crash App: " + e.getMessage());
                Toast.makeText(LoginActivity.this, "APP BỊ LỖI: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
    }
<<<<<<< HEAD
}
=======

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
>>>>>>> develop
