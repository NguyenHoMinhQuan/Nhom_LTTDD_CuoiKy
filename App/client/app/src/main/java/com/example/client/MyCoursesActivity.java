package com.example.client;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class MyCoursesActivity extends BaseActivity {
    private ImageView btnBack;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hocvien_mycourse);


        btnBack = findViewById(R.id.btnBack);

        // 2. Gọi hàm setup header (Xử lý Logo + Avatar)
        setupCommonHeader();


        //xử lý nút quy lại trang trước
        if(btnBack != null){
            btnBack.setOnClickListener(v -> {
                finish();// đóng trang này để lộ ra trang trước đó
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);

            });

        }


    }

}
