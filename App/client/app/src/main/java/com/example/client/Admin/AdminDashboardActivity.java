package com.example.client.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.client.ForgotPassActivity;
import com.example.client.R;

public class AdminDashboardActivity extends AppCompatActivity {

    private LinearLayout btnGoToUser, btnGoToCourse, btnGoToAnnouncement,btnGoToClass;
    private Button btnLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        btnGoToUser = findViewById(R.id.btnGoToUser);
        btnGoToCourse = findViewById(R.id.btnGoToCourse);
        btnGoToClass = findViewById(R.id.btnGoToClass);
        // btnLogout = findViewById(R.id.btnLogout);
        btnGoToAnnouncement = findViewById(R.id.btnGoToAnnouncement);

        // --- Nút Quản lý Người dùng ---
        if (btnGoToUser != null) {
            btnGoToUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AdminDashboardActivity.this, QuanLyUserActivity.class);
                    startActivity(intent);
                }
            });
        }
        // --- Nút Quản lý Khóa học ---
        if (btnGoToCourse != null) {
            btnGoToCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AdminDashboardActivity.this, QuanLyKhoaHocActivity.class);
                    startActivity(intent);
                }
            });
        }
        // --- Nút Quản lý Lớp học ---
        if (btnGoToClass != null) {
            btnGoToClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AdminDashboardActivity.this, QuanLyLopHocActivity.class);
                    startActivity(intent);
                }
            });
            // --- 4. Nút Quản lý Thông Báo (MỚI) ---
            if (btnGoToAnnouncement != null) {
                btnGoToAnnouncement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Chuyển sang trang QuanLyThongBaoActivity chúng ta vừa làm
                        Intent intent = new Intent(AdminDashboardActivity.this, QuanLyThongBaoActivity.class);
                        startActivity(intent);
                    }
                });
            }

            // --- 5. Nút Đăng Xuất (MỚI) ---
            if (btnLogout != null) {
                btnLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Chuyển về trang Login
                        Intent intent = new Intent(AdminDashboardActivity.this, ForgotPassActivity.class);

                        // Cờ (Flag) này để xóa hết các Activity cũ,
                        // giúp người dùng không thể bấm nút Back để quay lại Dashboard
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);
                        finish(); // Đóng Activity hiện tại
                    }
                });
            }
        }
    }
}