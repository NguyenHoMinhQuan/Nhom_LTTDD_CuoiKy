package com.example.client.lecturer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Toast;

import com.example.client.R;
import com.example.client.api.ApiService;
import com.example.client.lecturer.adapter.ScheduleAdapter;
import com.example.client.lecturer.model.ScheduleItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Activity này cần implements Listener để xử lý click
public class TimetableActivity extends AppCompatActivity implements ScheduleAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private List<ScheduleItem> scheduleList;
    private ApiService apiService;
    private static String BASE_URL ="http://10.0.2.2:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sử dụng layout giangvien_timetable_layout.xml
        setContentView(R.layout.lecturer_timetable);

        recyclerView = findViewById(R.id.recycler_schedule);

        InitRetrofit();
        fetchLecturerSchedule(2);
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

    private void InitRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    private void fetchLecturerSchedule(Integer lecturerId) {
        apiService.getScheduleByLecturerId(lecturerId).enqueue(new Callback<List<ScheduleItem>>() {
            @Override
            public void onResponse(Call<List<ScheduleItem>> call, Response<List<ScheduleItem>> response) {
                if(response.isSuccessful() && response.body()!=null) {
                    List<ScheduleItem> schedule = response.body();

                    if(schedule.isEmpty()) {
                        Toast.makeText(TimetableActivity.this,"Không tìm thấy lịch học", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Toast.makeText(TimetableActivity.this,
                            "Đã lấy" + schedule.size() + "buổi học thành công",
                            Toast.LENGTH_LONG
                    ).show();

                    setupTimetableRecycleView(schedule);
                }
            }

            @Override
            public void onFailure(Call<List<ScheduleItem>> call, Throwable t) {

            }
        });
    }

    private void setupTimetableRecycleView(List<ScheduleItem> schedule) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        ScheduleAdapter adapter = new ScheduleAdapter(schedule, this);
        recyclerView.setAdapter(adapter);
    }

}
