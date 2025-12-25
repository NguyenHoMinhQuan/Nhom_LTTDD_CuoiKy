package com.example.client.HocVien;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.HocVien.Models.TinNhanModel;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    // Dữ liệu nhận từ Intent
    int idLop = 0;          // Dùng để GỬI tin nhắn (Insert DB)
    String classCode = "";  // Dùng để LẤY danh sách tin nhắn (Select DB)
    String tenLop = "";     // Hiển thị tiêu đề

    // Thông tin người dùng hiện tại
    String currentUsername = "";
    int currentUserId = 0;

    RecyclerView rv;
    EditText edtInput;
    Button btnGui;
    ImageView btnBack, btnFeedback;
    TextView tvTitle;

    List<TinNhanModel> listChat = new ArrayList<>();
    TinNhanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hocvien_activity_chat);

        // 1. Lấy thông tin User đăng nhập (Từ SharedPreferences)
        layThongTinUser();

        // 2. Nhận dữ liệu từ Adapter truyền sang
        Intent intent = getIntent();
        idLop = intent.getIntExtra("ID_LOP", 0);
        classCode = intent.getStringExtra("MA_LOP"); // Nhận Mã Lớp (ví dụ: CT101_B)
        tenLop = intent.getStringExtra("TEN_LOP");

        // 3. Ánh xạ View
        initViews();

        if (tenLop != null) tvTitle.setText(tenLop);

        // 4. Cấu hình RecyclerView
        adapter = new TinNhanAdapter(listChat, currentUsername);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        // 5. Load dữ liệu (Quan trọng: Dùng classCode)
        if (classCode != null && !classCode.isEmpty()) {
            loadData();
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy mã lớp!", Toast.LENGTH_SHORT).show();
        }

        // 6. Xử lý sự kiện
        handleEvents();
    }

    private void layThongTinUser() {
        // Giả lập lấy từ SharedPreferences (Bạn thay bằng key thực tế của bạn)
        SharedPreferences prefs = getSharedPreferences("AUTH_PREFS", MODE_PRIVATE);
        currentUsername = prefs.getString("USERNAME", "student1"); // Mặc định student1 để test
        currentUserId = prefs.getInt("USER_ID", 1);
    }

    private void initViews() {
        rv = findViewById(R.id.rvTinNhan);
        edtInput = findViewById(R.id.edtChatInput);
        btnGui = findViewById(R.id.btnChatSend);
        btnBack = findViewById(R.id.btnBackChat);
        btnFeedback = findViewById(R.id.btnGoToFeedback);
        tvTitle = findViewById(R.id.tvTitleLop);
    }

    private void handleEvents() {
        btnBack.setOnClickListener(v -> finish());

        // Chuyển sang Feedback
        btnFeedback.setOnClickListener(v -> {
            Intent intent = new Intent(ChatActivity.this, FeedbackActivity.class);
            intent.putExtra("ID_LOP", idLop);
            intent.putExtra("TEN_LOP", tenLop);
            startActivity(intent);
        });

        // Gửi tin nhắn
        btnGui.setOnClickListener(v -> {
            String txt = edtInput.getText().toString().trim();
            if (!txt.isEmpty() && idLop > 0) {
                // Tạo model gửi đi (Dùng classId để Insert vào bảng ClassMessage)
                TinNhanModel msg = new TinNhanModel(idLop, currentUserId, txt);

                ApiClient.getClient(this).create(ApiService.class).guiTinNhan(msg).enqueue(new Callback<TinNhanModel>() {
                    @Override
                    public void onResponse(Call<TinNhanModel> call, Response<TinNhanModel> response) {
                        if (response.isSuccessful()) {
                            edtInput.setText("");
                            loadData(); // Load lại tin nhắn sau khi gửi
                        } else {
                            Toast.makeText(ChatActivity.this, "Gửi thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TinNhanModel> call, Throwable t) {
                        Toast.makeText(ChatActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // --- HÀM LOAD TIN NHẮN THEO CLASS CODE ---
    void loadData() {
        ApiService api = ApiClient.getClient(this).create(ApiService.class);

        // Gọi API GET: /api/student/chat?username=...&classCode=...
        api.layTinNhan(currentUsername, classCode).enqueue(new Callback<List<TinNhanModel>>() {
            @Override
            public void onResponse(Call<List<TinNhanModel>> call, Response<List<TinNhanModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listChat.clear();
                    listChat.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    // Cuộn xuống tin nhắn mới nhất
                    if (!listChat.isEmpty()) {
                        rv.scrollToPosition(listChat.size() - 1);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TinNhanModel>> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Không thể tải tin nhắn: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}