package com.example.client.lecturer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView; // Import nếu bạn có nút back là ImageView
import android.widget.Toast;

import com.example.client.Login.LoginActivity;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
import com.example.client.lecturer.adapter.NotificationAdapter;
import com.example.client.lecturer.model.NotificationItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity implements NotificationAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private ApiService apiService;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_announcement);

        // 🟢 1. LẤY USER_ID TỪ PREFS
        SharedPreferences prefs = getSharedPreferences("AUTH_PREFS", MODE_PRIVATE);
        currentUserId = prefs.getInt("USER_ID", -1);

        // Kiểm tra đăng nhập
        if (currentUserId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // 🟢 2. ÁNH XẠ VIEW
        recyclerView = findViewById(R.id.recycler_announcement);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initRetrofit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 🟢 3. GỌI API KHI MÀN HÌNH HIỆN LÊN (Để refresh danh sách khi quay lại từ chi tiết)
        if (currentUserId != -1) {
            fetchAllNotifications(currentUserId);
        }
    }

    private void initRetrofit() {
        apiService = ApiClient.getClient(this).create(ApiService.class);
    }

    private void fetchAllNotifications(Integer userId) {
        // Gọi API lấy toàn bộ thông báo (ID động)
        apiService.getAllNotifications(userId).enqueue(new Callback<List<NotificationItem>>() {
            @Override
            public void onResponse(Call<List<NotificationItem>> call, Response<List<NotificationItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NotificationItem> list = response.body();

                    // Kiểm tra nếu list rỗng
                    if (list.isEmpty()) {
                        Toast.makeText(NotificationActivity.this, "Bạn chưa có thông báo nào", Toast.LENGTH_SHORT).show();
                    }

                    // Gán adapter với dữ liệu thật
                    adapter = new NotificationAdapter(list, NotificationActivity.this);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<NotificationItem>> call, Throwable t) {
                Toast.makeText(NotificationActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(NotificationItem item) {
        // 1. Gọi API đánh dấu đã đọc
        markAsRead(item.getNotificationId());

        // 2. Chuyển sang màn hình chi tiết
        Intent detailIntent = new Intent(this, NotificationDetailActivity.class);
        detailIntent.putExtra("NOTIFICATION_DATA", item); // Đảm bảo class NotificationItem đã implements Serializable
        startActivity(detailIntent);
    }

    private void markAsRead(Integer id) {
        apiService.markAsRead(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Thành công: Backend đã update IsRead = true
                // Khi user quay lại (onResume), danh sách sẽ tự reload lại trạng thái mới
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Log lỗi nếu cần
            }
        });
    }
}