package com.example.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast; // Thư viện thông báo
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private LinearLayout btnGoToUser, btnGoToCourse, btnGoToClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 1. QUAN TRỌNG: Phải liên kết đúng file activity_main.xml
        setContentView(R.layout.activity_main);

        // 2. Ánh xạ (Tìm view theo ID bạn đã đặt trong XML)
        btnGoToUser = findViewById(R.id.btnGoToUser);
        btnGoToCourse = findViewById(R.id.btnGoToCourse);
        btnGoToClass = findViewById(R.id.btnGoToClass);

        // 3. Gắn sự kiện Click

        // --- Nút User ---
        btnGoToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiện thông báo nhỏ để biết nút đã nhận click
                Toast.makeText(MainActivity.this, "Click User", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, QuanLyUserActivity.class);
                startActivity(intent);
            }
        });

        // --- Nút Khóa học ---
        btnGoToCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Click Khóa học", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, QuanLyKhoaHocActivity.class);
                startActivity(intent);
            }
        });

        // --- Nút Lớp học ---
        btnGoToClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Click Lớp học", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, QuanLyLopHocActivity.class);
                startActivity(intent);
            }
        });
    }
}