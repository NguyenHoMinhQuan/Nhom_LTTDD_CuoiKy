package com.example.client;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class HomeActivity extends BaseActivity {
    //Khai báo các thuộc tính
    private ImageView imgAvatar;
    private TextView tvSearch;
    private RecyclerView rvHomeCourses;

    //private CourseAdapter courseAdapter; // Adapter quản lý danh sách


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_hv);

        // 2. Gọi hàm setup header (Tự động xử lý Logo và Avatar)
        setupCommonHeader();

        //Ánh xạ view từ layout

        rvHomeCourses = findViewById(R.id.rvCourseList1);
        tvSearch = findViewById(R.id.tvSearchTitle);


        // cài đặt các chức năng
        setupNavigation();// xử lý chuyển trang
        //setupCourseList();// hiển thị danh sách khóa học

    }

    //Hàm xử lý chuyển trang
    private void setupNavigation() {
        //sự kiện click nút tìm kiếm
        tvSearch.setOnClickListener(v -> {
            navigate(SearchActivity.class);
        });

    }
    //Hàm xử lý hiện danh sách khóa học ...

}
