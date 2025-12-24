package com.example.client.lecturer.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.api.ApiClient; // Đã dùng đúng ApiClient của bạn
import com.example.client.api.ApiService;
import com.example.client.lecturer.adapter.ChatAdapter;
import com.example.client.lecturer.model.ChatMessageDTO;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Callback;

public class ChatActivity extends AppCompatActivity {
    // Sử dụng okhttp3.WebSocket để không bị lỗi "Cannot resolve symbol StompClient"
    private WebSocket webSocket;
    private ChatAdapter adapter;
    private List<ChatMessageDTO> messageList = new ArrayList<>();
    private RecyclerView recyclerView;
    private EditText etMessageInput;
    private ImageButton btnSend;

    // Các thông số định danh (Bạn có thể lấy từ Intent hoặc SharedPreferences)
    private int currentUserId = 2;
    private int classId = 1;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // BƯỚC QUAN TRỌNG: Lấy classId từ Intent thay vì gán cứng = 1
        // "CLASS_ID" phải khớp với tên key bạn đã putExtra bên ChatListActivity
        classId = getIntent().getIntExtra("CLASS_ID", -1);

        // (Tùy chọn) Lấy thêm tên lớp để hiển thị lên tiêu đề cho đỡ nhầm
        String className = getIntent().getStringExtra("CLASS_NAME");
        if (getSupportActionBar() != null && className != null) {
            getSupportActionBar().setTitle(className);
        }

        // Nếu không nhận được ID lớp thì báo lỗi và đóng màn hình để tránh sai dữ liệu
        if (classId == -1) {
            Toast.makeText(this, "Không tìm thấy mã lớp!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        loadChatHistory();
        startWebSocket();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rv_chat_messages);
        etMessageInput = findViewById(R.id.et_message_input);
        btnSend = findViewById(R.id.btn_send_message);

        adapter = new ChatAdapter(messageList, currentUserId);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnSend.setOnClickListener(v -> {
            String content = etMessageInput.getText().toString().trim();
            if (!content.isEmpty()) {
                sendMessage(content);
                etMessageInput.setText("");
            }
        });
    }

    private void loadChatHistory() {
        // Truyền 'this' vào ApiClient.getClient(context) đúng như bạn đã viết
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        apiService.getChatHistory(classId).enqueue(new Callback<List<ChatMessageDTO>>() {
            @Override
            public void onResponse(Call<List<ChatMessageDTO>> call, retrofit2.Response<List<ChatMessageDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    messageList.clear();
                    messageList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<List<ChatMessageDTO>> call, Throwable t) {
                Log.e("CHAT_LOG", "Lỗi lịch sử: " + t.getMessage());
            }
        });
    }

    private void startWebSocket() {
        OkHttpClient client = new OkHttpClient();
        // Địa chỉ phải khớp với registry.addHandler(..., "/chat-socket") ở Backend
        Request request = new Request.Builder()
                .url("ws://10.0.2.2:9000/chat-socket")
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                ChatMessageDTO msg = new Gson().fromJson(text, ChatMessageDTO.class);

                // CHỈ HIỂN THỊ NẾU classId CỦA TIN NHẮN TRÙNG VỚI LỚP ĐANG MỞ
                if (msg.getClassId() != null && msg.getClassId() == classId) {
                    runOnUiThread(() -> {
                        messageList.add(msg);
                        adapter.notifyItemInserted(messageList.size() - 1);
                        recyclerView.scrollToPosition(messageList.size() - 1);
                    });
                }
            }
        });
    }

    private void sendMessage(String content) {
        ChatMessageDTO msg = new ChatMessageDTO(classId, currentUserId, content);
        String json = gson.toJson(msg);
        if (webSocket != null) {
            webSocket.send(json); // Gửi chuỗi JSON qua WebSocket
        } else {
            Toast.makeText(this, "Chưa kết nối server", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocket != null) {
            webSocket.close(1000, "Activity Destroyed");
        }
    }
}