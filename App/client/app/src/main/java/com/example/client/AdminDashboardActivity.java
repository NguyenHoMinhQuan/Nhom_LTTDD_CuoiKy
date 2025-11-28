package com.example.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private LinearLayout btnGoToUser, btnGoToCourse, btnGoToClass;
    private Button btnLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        btnGoToUser = findViewById(R.id.btnGoToUser);
        btnGoToCourse = findViewById(R.id.btnGoToCourse);
        btnGoToClass = findViewById(R.id.btnGoToClass);
        // btnLogout = findViewById(R.id.btnLogout);


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
        }
    }
}