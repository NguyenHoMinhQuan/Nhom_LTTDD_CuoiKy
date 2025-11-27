package com.example.client;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MyCoursesActivity extends BaseActivity {
    private ImageView btnBack;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses_hv);


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
