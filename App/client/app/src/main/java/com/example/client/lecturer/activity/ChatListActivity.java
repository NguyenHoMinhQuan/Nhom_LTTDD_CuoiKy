package com.example.client.lecturer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
import com.example.client.lecturer.adapter.ChatClassAdapter;
import com.example.client.lecturer.model.ClassDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ApiService apiService;
    private ChatClassAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        recyclerView = findViewById(R.id.rv_chat_list_classes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dùng ApiClient có sẵn Token của bạn
        apiService = ApiClient.getClient(this).create(ApiService.class);

        loadLecturerClasses();
    }

    private void loadLecturerClasses() {
        // Giả sử ID giảng viên là 2
        apiService.getClassesByLecturer(2).enqueue(new Callback<List<ClassDTO>>() {
            @Override
            public void onResponse(Call<List<ClassDTO>> call, Response<List<ClassDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setupAdapter(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<ClassDTO>> call, Throwable t) {
                Toast.makeText(ChatListActivity.this, "Lỗi tải danh sách lớp", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupAdapter(List<ClassDTO> list) {
        adapter = new ChatClassAdapter(list, classItem -> {
            // Khi chọn 1 lớp, chuyển sang ChatActivity và mang theo dữ liệu lớp đó
            Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
            intent.putExtra("CLASS_ID", classItem.getClassId());
            intent.putExtra("CLASS_NAME", classItem.getClassCode());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
}
