package com.example.client;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {
    private ImageView btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_hv);

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
