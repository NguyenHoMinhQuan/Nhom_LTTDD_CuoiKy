package com.example.client.HocVien;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.client.R;

public class CalenderSchoolActivity extends BaseHocVienActivity {
    private TextView btnLui;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hocvien_layout_calender_school);

        btnLui = findViewById(R.id.btnLui);

        // Xử lý sự kiện
        if(btnLui != null){
            btnLui.setOnClickListener(v -> {
                finish();
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
            });
        }
    }
}
