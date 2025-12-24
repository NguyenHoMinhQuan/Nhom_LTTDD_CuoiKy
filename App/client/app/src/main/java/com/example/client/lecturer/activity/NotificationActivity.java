package com.example.client.lecturer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;

import java.util.List;
import android.content.Intent;
import android.widget.Toast;

import com.example.client.R;
import com.example.client.api.ApiService;
import com.example.client.lecturer.adapter.NotificationAdapter;
import com.example.client.lecturer.model.NotificationItem; // Dùng model mới

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationActivity extends AppCompatActivity implements NotificationAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private ApiService apiService;
    private static final String BASE_URL = "http://10.0.2.2:9000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_announcement);

        recyclerView = findViewById(R.id.recycler_announcement);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initRetrofit();

        // Lấy TẤT CẢ thông báo của giảng viên (ID = 2)
        fetchAllNotifications(2);
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    private void fetchAllNotifications(Integer userId) {
        // Gọi API lấy toàn bộ thông báo (bao gồm cả đã đọc và chưa đọc)
        apiService.getAllNotifications(userId).enqueue(new Callback<List<NotificationItem>>() {
            @Override
            public void onResponse(Call<List<NotificationItem>> call, Response<List<NotificationItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NotificationItem> list = response.body();

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
        // Khi click vào thông báo ở trang danh sách, cũng nên đánh dấu đã đọc
        markAsRead(item.getNotificationId());

        Intent detailIntent = new Intent(this, NotificationDetailActivity.class);
        detailIntent.putExtra("NOTIFICATION_DATA", item);
        startActivity(detailIntent);
    }

    private void markAsRead(Integer id) {
        apiService.markAsRead(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Ở trang "Tất cả", ta không cần xóa item đi, chỉ cần cập nhật trạng thái IsRead trong DB
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }
}