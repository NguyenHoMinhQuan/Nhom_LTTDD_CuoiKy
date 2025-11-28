package com.example.client.HocVien;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.client.R;

public class SearchActivity extends AppCompatActivity {
    private ImageView btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hocvien_search);

        btnBack = findViewById(R.id.btnBack);
        //xử lý nút quy lại trang trước
        if(btnBack != null){
            btnBack.setOnClickListener(v -> {
                finish();// đóng trang này để lộ ra trang trước đó
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);

            });

        }
    }
}
