package com.example.client.lecturer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.client.Login.LoginActivity;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
import com.example.client.lecturer.adapter.ScheduleAdapter;
import com.example.client.lecturer.model.ScheduleItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Activity hiển thị toàn bộ lịch dạy của Giảng viên
public class TimetableActivity extends AppCompatActivity implements ScheduleAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ApiService apiService;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_timetable);

        // 🟢 1. LẤY USER_ID TỪ PREFS
        SharedPreferences prefs = getSharedPreferences("AUTH_PREFS", MODE_PRIVATE);
        currentUserId = prefs.getInt("USER_ID", -1);

        // Kiểm tra bảo mật
        if (currentUserId == -1) {
            Toast.makeText(this, "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        recyclerView = findViewById(R.id.recycler_schedule);

        InitRetrofit();

        // 🟢 2. GỌI API VỚI ID THỰC TẾ
        fetchLecturerSchedule(currentUserId);
    }

    private void InitRetrofit() {
        apiService = ApiClient.getClient(this).create(ApiService.class);
    }

    private void fetchLecturerSchedule(Integer lecturerId) {
        apiService.getScheduleByLecturerId(lecturerId).enqueue(new Callback<List<ScheduleItem>>() {
            @Override
            public void onResponse(Call<List<ScheduleItem>> call, Response<List<ScheduleItem>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<ScheduleItem> schedule = response.body();

                    if(schedule.isEmpty()) {
                        Toast.makeText(TimetableActivity.this,"Chưa có lịch dạy nào", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Toast.makeText(TimetableActivity.this, "Đã tải " + schedule.size() + " buổi học", Toast.LENGTH_SHORT).show();
                    setupTimetableRecycleView(schedule);
                }
            }

            @Override
            public void onFailure(Call<List<ScheduleItem>> call, Throwable t) {
                // Hiển thị lỗi ra để biết đường sửa
                Toast.makeText(TimetableActivity.this, "Lỗi tải lịch: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupTimetableRecycleView(List<ScheduleItem> schedule) {
        // Hiển thị dạng danh sách dọc (Vertical) cho đầy đủ thông tin
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ScheduleAdapter adapter = new ScheduleAdapter(schedule, this);
        recyclerView.setAdapter(adapter);
    }

    // Triển khai phương thức click
    @Override
    public void onItemClick(ScheduleItem item) {
        // Xử lý khi click vào một buổi học
        Toast.makeText(this, "Môn: " + item.getCourseName() + "\nPhòng: " + item.getRoom(), Toast.LENGTH_SHORT).show();

        // Nếu bạn muốn làm chức năng điểm danh hoặc xem chi tiết,
        // thì Intent sang Activity khác tại đây:
        /*
        Intent intent = new Intent(this, ScheduleDetailActivity.class);
        intent.putExtra("SCHEDULE_ID", item.getScheduleId());
        startActivity(intent);
        */
    }
}