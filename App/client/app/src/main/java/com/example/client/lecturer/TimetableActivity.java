package com.example.client.lecturer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Toast;

import com.example.client.R;
import com.example.client.lecturer.adapter.ScheduleAdapter;
import com.example.client.lecturer.model.ScheduleItem;

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
        setContentView(R.layout.lecturer_timetable);

        recyclerView = findViewById(R.id.recycler_schedule);

        // 1. Tạo dữ liệu mẫu

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


}
