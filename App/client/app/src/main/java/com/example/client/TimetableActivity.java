package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

// Activity này cần implements Listener để xử lý click
public class TimetableActivity extends AppCompatActivity implements ScheduleAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private List<ScheduleItem> scheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sử dụng layout giangvien_timetable_layout.xml
        setContentView(R.layout.giangvien_timetable_layout);

        recyclerView = findViewById(R.id.recycler_schedule);

        // 1. Tạo dữ liệu mẫu
        scheduleList = generateDummySchedule();

        // 2. Tạo Adapter, truyền dữ liệu và 'this' (Listener)
        ScheduleAdapter adapter = new ScheduleAdapter(scheduleList, this);

        // 3. Thiết lập Layout Manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 4. Gán Adapter cho RecyclerView
        recyclerView.setAdapter(adapter);
    }

    // Triển khai phương thức click
    @Override
    public void onItemClick(ScheduleItem item) {
        // Xử lý khi click vào một buổi học
        // Ví dụ: Mở màn hình chi tiết buổi học (nếu có) hoặc hiển thị thông báo
        Toast.makeText(this, "Buổi học: " + item.getCourseName() + " tại " + item.getRoom(), Toast.LENGTH_SHORT).show();

        // Logic để mở Activity chi tiết sẽ ở đây (tương tự như Announcement)
        // Intent detailIntent = new Intent(this, ScheduleDetailActivity.class);
        // detailIntent.putExtra("EXTRA_SCHEDULE_ITEM", item);
        // startActivity(detailIntent);
    }

    private List<ScheduleItem> generateDummySchedule() {
        List<ScheduleItem> list = new ArrayList<>();
        list.add(new ScheduleItem(
                "Thứ Hai", "Lập trình Di động Nâng cao", "07:30", "10:45", "A301", "PGS.TS Nguyễn Văn A"));
        list.add(new ScheduleItem(
                "Thứ Ba", "Cơ sở Dữ liệu Phân tán", "13:00", "16:15", "B205", "TS. Lê Thị B"));
        list.add(new ScheduleItem(
                "Thứ Tư", "Trí tuệ Nhân tạo", "07:30", "09:00", "C102", "PGS.TS Nguyễn Văn A"));
        list.add(new ScheduleItem(
                "Thứ Năm", "Mạng Máy tính", "14:45", "18:00", "D401", "ThS. Trần Đình C"));
        // Thêm nhiều mục khác...
        return list;
    }
}
