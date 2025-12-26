package com.example.client.HocVien;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.HocVien.Adapter.MyCourseAdapter;
import com.example.client.HocVien.Models.HocVien_NhomLopDto;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCoursesActivity extends BaseHocVienActivity {
    private RecyclerView recyclerViewCourses;
    private MyCourseAdapter adapter;
    private ImageView btnBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hocvien_mycourse);

        // Ánh xạ
        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);
        btnBack = findViewById(R.id.btnBack);

        // Cài đặt RecyclerView
        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(this));

        // Xử lý nút Back
        btnBack.setOnClickListener(v -> finish());

        // ấy username và gọi API
        SharedPreferences prefs = getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE);
        String username = prefs.getString("USERNAME", "");

        if (!username.isEmpty()) {
            loadMyCourses(username);
        } else {
            Toast.makeText(this, "Chưa đăng nhập!", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadMyCourses(String username) {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        // Gọi API lấy danh sách lớp
        apiService.LayNhomLopSinhVien(username).enqueue(new Callback<List<HocVien_NhomLopDto>>() {
            @Override
            public void onResponse(Call<List<HocVien_NhomLopDto>> call, Response<List<HocVien_NhomLopDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<HocVien_NhomLopDto> listCourses = response.body();

                    // Gắn vào Adapter
                    adapter = new MyCourseAdapter(listCourses);
                    recyclerViewCourses.setAdapter(adapter);

                    if(listCourses.isEmpty()){
                        Toast.makeText(MyCoursesActivity.this, "Bạn chưa đăng ký lớp nào.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MyCoursesActivity.this, "Không lấy được dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<HocVien_NhomLopDto>> call, Throwable t) {
                Toast.makeText(MyCoursesActivity.this, "Lỗi kết nối Server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}