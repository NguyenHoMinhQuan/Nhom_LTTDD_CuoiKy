package com.example.client.lecturer.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.client.R;
import com.example.client.lecturer.model.NotificationItem;

public class NotificationDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_announcement_detail);

        // 1. SỬA: Lấy dữ liệu NotificationItem từ Intent (Khớp với khóa "NOTIFICATION_DATA" đã gửi từ Dashboard)
        NotificationItem item = (NotificationItem) getIntent().getSerializableExtra("NOTIFICATION_DATA");

        if (item != null) {
            // Ánh xạ View
            TextView titleTextView = findViewById(R.id.tv_detail_title);
            TextView authorTextView = findViewById(R.id.tv_detail_author); // Nếu DB có Author thì hiển thị
            TextView dateTextView = findViewById(R.id.tv_detail_created_at);
            TextView bodyTextView = findViewById(R.id.tv_detail_body);

            // 2. Đổ dữ liệu THẬT từ Database
            titleTextView.setText(item.getTitle());
            bodyTextView.setText(item.getBody());
            dateTextView.setText("Ngày đăng: " + item.getCreatedAt());

            // Vì NotificationItem hiện tại chưa có trường Author, bạn có thể để mặc định
            // hoặc bổ sung Author vào DTO ở Backend nếu cần.
            authorTextView.setText("Thông báo hệ thống");

            // Đặt tiêu đề trên thanh ActionBar
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Chi tiết thông báo");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Thêm nút quay lại
            }
        }
    }

    // Xử lý nút quay lại trên ActionBar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}