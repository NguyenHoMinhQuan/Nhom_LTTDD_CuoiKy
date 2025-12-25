package com.example.client.lecturer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.Login.LoginActivity;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
import com.example.client.lecturer.adapter.ChatAdapter;
import com.example.client.lecturer.model.ChatMessageDTO;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    // WebSocket Variables
    private WebSocket webSocket;
    private final OkHttpClient client = new OkHttpClient(); // Nên khai báo 1 lần

    // UI Variables
    private ChatAdapter adapter;
    private List<ChatMessageDTO> messageList = new ArrayList<>();
    private RecyclerView recyclerView;
    private EditText etMessageInput;
    private ImageButton btnSend;

    // Data Variables
    private int currentUserId;
    private int classId;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 🟢 1. LẤY ID NGƯỜI DÙNG TỪ PREFS
        SharedPreferences prefs = getSharedPreferences("AUTH_PREFS", MODE_PRIVATE);
        currentUserId = prefs.getInt("USER_ID", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // 🟢 2. LẤY ID LỚP TỪ INTENT
        classId = getIntent().getIntExtra("CLASS_ID", -1);
        String className = getIntent().getStringExtra("CLASS_NAME");

        if (classId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy lớp học", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Setup Toolbar Title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(className != null ? className : "Chat Nhóm");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiện nút Back
        }

        initViews();
        loadChatHistory();
        startWebSocket();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Xử lý nút Back trên toolbar
        return true;
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rv_chat_messages);
        etMessageInput = findViewById(R.id.et_message_input);
        btnSend = findViewById(R.id.btn_send_message);

        // Truyền currentUserId thật vào để Adapter phân biệt tin nhắn của mình/người khác
        adapter = new ChatAdapter(messageList, currentUserId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // layoutManager.setStackFromEnd(true); // Tùy chọn: Luôn hiển thị tin nhắn mới nhất ở dưới cùng
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        btnSend.setOnClickListener(v -> {
            String content = etMessageInput.getText().toString().trim();
            if (!content.isEmpty()) {
                sendMessage(content);
                etMessageInput.setText("");
            }
        });
    }

    private void loadChatHistory() {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        apiService.getChatHistory(classId).enqueue(new Callback<List<ChatMessageDTO>>() {
            @Override
            public void onResponse(Call<List<ChatMessageDTO>> call, Response<List<ChatMessageDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    messageList.clear();
                    messageList.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    // Cuộn xuống tin nhắn cuối cùng
                    if (!messageList.isEmpty()) {
                        recyclerView.scrollToPosition(messageList.size() - 1);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ChatMessageDTO>> call, Throwable t) {
                Log.e("CHAT_LOG", "Lỗi tải lịch sử chat: " + t.getMessage());
            }
        });
    }

    private void startWebSocket() {
        // Lưu ý: 10.0.2.2 là localhost của máy tính khi chạy Emulator
        // Nếu chạy trên điện thoại thật, phải đổi thành IP LAN (VD: 192.168.1.X)
        Request request = new Request.Builder()
                .url("ws://10.0.2.2:9000/chat-socket")
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                super.onOpen(webSocket, response);
                Log.d("WEBSOCKET", "Connected");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                try {
                    ChatMessageDTO msg = gson.fromJson(text, ChatMessageDTO.class);

                    // 🟢 QUAN TRỌNG: Chỉ nhận tin nhắn của Lớp hiện tại
                    if (msg.getClassId() != null && msg.getClassId() == classId) {
                        runOnUiThread(() -> {
                            messageList.add(msg);
                            adapter.notifyItemInserted(messageList.size() - 1);
                            recyclerView.smoothScrollToPosition(messageList.size() - 1);
                        });
                    }
                } catch (Exception e) {
                    Log.e("WEBSOCKET", "Lỗi parse JSON: " + e.getMessage());
                }
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                Log.d("WEBSOCKET", "Closed: " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                super.onFailure(webSocket, t, response);
                Log.e("WEBSOCKET", "Error: " + t.getMessage());
                // Có thể thêm logic reconnect ở đây nếu muốn
            }
        });
    }

    private void sendMessage(String content) {
        // Tạo tin nhắn với ID thật
        ChatMessageDTO msg = new ChatMessageDTO(classId, currentUserId, content);

        // Có thể thêm tên người gửi nếu DTO hỗ trợ để hiển thị ngay lập tức
        // msg.setSenderName("Me");

        String json = gson.toJson(msg);

        if (webSocket != null) {
            boolean sent = webSocket.send(json);
            if (!sent) {
                Toast.makeText(this, "Mất kết nối server, đang thử lại...", Toast.LENGTH_SHORT).show();
                // Logic reconnect có thể đặt ở đây
            }
        } else {
            Toast.makeText(this, "Chưa kết nối server", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đóng socket khi thoát màn hình để tiết kiệm tài nguyên
        if (webSocket != null) {
            webSocket.close(1000, "Activity Destroyed");
        }
    }
}