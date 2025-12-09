package com.example.client.lecturer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.client.R;
import com.example.client.lecturer.model.Announcement;

public class AnnouncementDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_announcement_detail);

        // Lấy dữ liệu từ Intent
        Announcement announcement = (Announcement) getIntent().getSerializableExtra(AnnouncementActivity.EXTRA_ANNOUNCEMENT);

        if (announcement != null) {
            // Ánh xạ View
            TextView titleTextView = findViewById(R.id.tv_detail_title);
            TextView authorTextView = findViewById(R.id.tv_detail_author);
            TextView dateTextView = findViewById(R.id.tv_detail_created_at);
            TextView bodyTextView = findViewById(R.id.tv_detail_body);

            // Đổ dữ liệu vào View
            titleTextView.setText(announcement.getTitle());

            // Lưu ý: Trong Announcement model hiện tại chỉ có Title và Summary.
            // Bạn có thể dùng Summary làm Body chi tiết, hoặc tạo thêm một thuộc tính Body đầy đủ.
            // Tạm thời hiển thị Author và Date từ model.
            authorTextView.setText("Tác giả: " + announcement.getAuthor());
            dateTextView.setText("Ngày: " + announcement.getCreatedAt());
            bodyTextView.setText(announcement.getBody()); // Thay bằng getBody() nếu có

            // Đặt tiêu đề trên thanh ActionBar (nếu có)
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(announcement.getTitle());
            }
        }
    }
}
