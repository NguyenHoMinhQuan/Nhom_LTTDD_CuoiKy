package com.example.client.HocVien;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.client.R;

public class ProfileActivity extends AppCompatActivity {

    private View btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hocvien_layout_infor_student);

        // 2. SỬA ID: Tìm theo ID mới @+id/btnBack đã sửa ở bước 1
        btnBack = findViewById(R.id.btnBack);

        // Xử lý sự kiện
        if(btnBack != null){
            btnBack.setOnClickListener(v -> {
                finish();
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
            });
        }
    }
}
