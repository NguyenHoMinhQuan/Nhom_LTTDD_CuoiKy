package com.example.client.lecturer.activity;

import android.content.Intent;
import android.content.SharedPreferences; // Import thêm
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.Login.LoginActivity; // Import Login
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

    // Biến lưu ID người dùng hiện tại
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        // 🟢 1. LẤY ID TỪ PREFS
        SharedPreferences prefs = getSharedPreferences("AUTH_PREFS", MODE_PRIVATE);
        currentUserId = prefs.getInt("USER_ID", -1);

        // Kiểm tra đăng nhập
        if (currentUserId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        recyclerView = findViewById(R.id.rv_chat_list_classes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dùng ApiClient có sẵn Token của bạn
        apiService = ApiClient.getClient(this).create(ApiService.class);

        // 🟢 2. GỌI HÀM LOAD DỮ LIỆU
        loadLecturerClasses();
    }

    private void loadLecturerClasses() {
        // 🟢 3. THAY SỐ 2 CỨNG BẰNG BIẾN currentUserId
        apiService.getClassesByLecturer(currentUserId).enqueue(new Callback<List<ClassDTO>>() {
            @Override
            public void onResponse(Call<List<ClassDTO>> call, Response<List<ClassDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ClassDTO> classes = response.body();

                    if (classes.isEmpty()) {
                        Toast.makeText(ChatListActivity.this, "Bạn chưa phụ trách lớp nào", Toast.LENGTH_SHORT).show();
                    }

                    setupAdapter(classes);
                } else {
                    Toast.makeText(ChatListActivity.this, "Không tải được danh sách lớp", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<ClassDTO>> call, Throwable t) {
                Toast.makeText(ChatListActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupAdapter(List<ClassDTO> list) {
        adapter = new ChatClassAdapter(list, classItem -> {
            // Khi chọn 1 lớp, chuyển sang ChatActivity
            Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
            intent.putExtra("CLASS_ID", classItem.getClassId());
            intent.putExtra("CLASS_NAME", classItem.getClassCode()); // Hoặc classItem.getClassName()

            // 🟢 4. GỬI THÊM ID NGƯỜI GỬI (để bên ChatActivity biết tin nhắn nào là của mình)
            intent.putExtra("SENDER_ID", currentUserId);

            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
}