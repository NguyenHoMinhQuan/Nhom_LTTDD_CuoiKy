package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.widget.Toast;

public class AnnouncementListActivity extends AppCompatActivity implements AnnouncementAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private AnnouncementAdapter adapter;
    private List<Announcement> announcementList;
    public static final String EXTRA_ANNOUNCEMENT = "extra_announcement";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Đảm bảo bạn đang sử dụng layout activity_announcement_list.xml
        setContentView(R.layout.activity_announcement_list);

        recyclerView = findViewById(R.id.recycler_announcement);

        // 1. Tạo dữ liệu mẫu
        announcementList = generateDummyAnnouncements();

        // 2. Tạo Adapter và gán dữ liệu
        adapter = new AnnouncementAdapter(announcementList,this);

        // 3. Thiết lập Layout Manager (quyết định cách các item hiển thị)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 4. Gán Adapter cho RecyclerView
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Announcement announcement) {
        // 1. Tạo Intent để mở Detail Activity
        // Giả sử Activity chi tiết của bạn tên là AnnouncementDetailActivity.java
        Intent detailIntent = new Intent(AnnouncementListActivity.this, AnnouncementDetailActivity.class);

        // 2. Truyền đối tượng Announcement sang Activity chi tiết
        detailIntent.putExtra(EXTRA_ANNOUNCEMENT, announcement);

        // 3. Khởi động Activity
        startActivity(detailIntent);

        // (Tùy chọn) Toast.makeText(this, "Bạn đã click: " + announcement.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private List<Announcement> generateDummyAnnouncements() {
        List<Announcement> list = new ArrayList<>();
        list.add(new Announcement(
                "Thông báo lịch thi cuối kỳ HK 1/2025",
                "Lịch thi chính thức cho học kỳ 1 năm học 2025 đã được cập nhật. Sinh viên kiểm tra...",
                "Phòng Đào tạo",
                "25/11/2025"));
        list.add(new Announcement(
                "Cảnh báo về tình hình dịch bệnh mới",
                "Yêu cầu toàn thể cán bộ, giảng viên và sinh viên thực hiện nghiêm túc các biện pháp...",
                "Phòng Công tác Sinh viên",
                "20/11/2025"));
        list.add(new Announcement(
                "Thông báo tuyển sinh sau đại học 2026",
                "Chi tiết về các ngành tuyển sinh, hồ sơ, và lịch thi tuyển đã được công bố...",
                "Phòng Sau Đại học",
                "10/11/2025"));
        return list;
    }
}
