package com.example.client.lecturer.activity;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client.R;
import com.example.client.api.ApiService;
import com.example.client.lecturer.adapter.AnnouncementAdapter;
import com.example.client.lecturer.adapter.ScheduleAdapter;
import com.example.client.lecturer.model.Announcement;
import com.example.client.lecturer.model.ScheduleItem;

import java.util.ArrayList;
import java.util.List;

public class LecturerDashboardActivity extends AppCompatActivity
        implements ScheduleAdapter.OnItemClickListener, AnnouncementAdapter.OnItemClickListener {

    private RecyclerView timetableRecyclerView;
    private RecyclerView announcementRecyclerView;
    private ImageView ivMessenger;

    private static final String BASE_URL = "http://10.0.2.2:9000/";
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_dashboard);

        // Khởi tạo các thành phần chính
        timetableRecyclerView = findViewById(R.id.recycler_timetable_today);
        announcementRecyclerView = findViewById(R.id.recycler_announcements_recent);
        ivMessenger = findViewById(R.id.iv_messenger);

        initRetrofit();

        setupQuickActions();

        fetchTodayLecturerSchedule(2);

        setupAnnouncementRecycler();

        setupHeader();

        ivMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LecturerDashboardActivity.this, AnnouncementActivity.class));
            }
        });
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    private void fetchTodayLecturerSchedule(Integer lecturerId) {
        apiService.getTodayScheduleByLecturerId(lecturerId).enqueue(new Callback<List<ScheduleItem>>() {
            @Override
            public void onResponse(Call<List<ScheduleItem>> call, Response<List<ScheduleItem>> response) {
                if (response.isSuccessful() & response.body()!= null) {
                    List<ScheduleItem> schedule = response.body();

                    if(schedule.isEmpty()) {
                        Toast.makeText(LecturerDashboardActivity.this,"Không tìm thấy lịch học", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Toast.makeText(LecturerDashboardActivity.this,
                            "Đã lấy" + schedule.size() + "buổi học thành công",
                            Toast.LENGTH_LONG
                    ).show();

                    setupTimetableRecycler(schedule);
                }
            }

            @Override
            public void onFailure(Call<List<ScheduleItem>> call, Throwable t) {
                Toast.makeText(LecturerDashboardActivity.this,"Lỗi kết nối Server: " + t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    /** Thiết lập Header: Hiển thị tên giảng viên */
    private void setupHeader() {
        TextView greetingTv = findViewById(R.id.tv_greeting);
        // Giả sử tên giảng viên được lấy từ dữ liệu đăng nhập
        String lecturerName = "Dr. Smith";
        greetingTv.setText(getString(R.string.greeting_format, lecturerName)); // Cần tạo string resource
    }

    /** Thiết lập Nút Hành động Nhanh */
    private void setupQuickActions() {
        // --- 1. Announcements ---
        View anncAction = findViewById(R.id.action_announcements);
        ImageView anncIcon = anncAction.findViewById(R.id.iv_action_icon);
        TextView anncText = anncAction.findViewById(R.id.tv_action_text);

        anncIcon.setImageResource(R.drawable.announcement);
        anncText.setText("Announcements");
        anncAction.setOnClickListener(v -> {
            // Mở màn hình danh sách thông báo
            startActivity(new Intent(this, AnnouncementActivity.class));
        });

        // --- 2. Timetable ---
        View ttAction = findViewById(R.id.action_timetable);
        ImageView ttIcon = ttAction.findViewById(R.id.iv_action_icon);
        TextView ttText = ttAction.findViewById(R.id.tv_action_text);

        ttIcon.setImageResource(R.drawable.timetable); // Thay bằng icon thực tế
        ttText.setText("Timetable");
        ttAction.setOnClickListener(v -> {
            // Mở màn hình thời khóa biểu đầy đủ
            startActivity(new Intent(this, TimetableActivity.class));
        });

        // --- 3. Assign Homework ---
        View hwAction = findViewById(R.id.action_assign_homework);
        ImageView hwIcon = hwAction.findViewById(R.id.iv_action_icon);
        TextView hwText = hwAction.findViewById(R.id.tv_action_text);

        hwIcon.setImageResource(R.drawable.assign); // Thay bằng icon thực tế
        hwText.setText("Assign Homework");
        hwAction.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng Giao bài tập", Toast.LENGTH_SHORT).show();
            Intent intentAssignment = new Intent(this, LecturerAssignmentActivity.class);
            startActivity(intentAssignment);
        });
    }


    private void setupTimetableRecycler(List<ScheduleItem> schedule) {
        timetableRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false));

        ScheduleAdapter adapter = new ScheduleAdapter(schedule, this);
        timetableRecyclerView.setAdapter(adapter);
    }

    /** Thiết lập RecyclerView cho Thông báo Gần đây (Vertical) */
    private void setupAnnouncementRecycler() {
        // Dữ liệu mẫu (chỉ lấy 3 mục gần đây nhất)
        List<Announcement> recentAnnouncements = generateDummyAnnouncements();

        // 1. Cấu hình LayoutManager: cuộn dọc
        // Lưu ý: Cần đảm bảo RecyclerView có height là wrap_content và nằm trong ScrollView
        announcementRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 2. Cấu hình Adapter
        AnnouncementAdapter adapter = new AnnouncementAdapter(recentAnnouncements, this);
        announcementRecyclerView.setAdapter(adapter);
    }

    // --- Xử lý Click ---

    // Click vào một buổi học trong Today's Timetable
    @Override
    public void onItemClick(ScheduleItem item) {
        Toast.makeText(this, "Chi tiết Buổi học: " + item.getCourseName(), Toast.LENGTH_SHORT).show();
        // Intent để mở màn hình chi tiết buổi học
        // Intent detailIntent = new Intent(this, ScheduleDetailActivity.class);
        // startActivity(detailIntent);
    }

    // Click vào một thông báo trong Recent Announcements
    @Override
    public void onItemClick(Announcement announcement) {
        // Mở màn hình chi tiết thông báo
        Intent detailIntent = new Intent(this, AnnouncementDetailActivity.class);
        detailIntent.putExtra(AnnouncementActivity.EXTRA_ANNOUNCEMENT, announcement); // Sử dụng lại hằng số đã định nghĩa
        startActivity(detailIntent);
    }

    // --- Phương thức tạo dữ liệu mẫu (Cần có trong Activity hoặc ViewModel) ---

    private List<Announcement> generateDummyAnnouncements() {
        List<Announcement> list = new ArrayList<>();
        list.add(new Announcement("Mid-term Exam Schedule", "Lịch thi giữa kỳ đã được cập nhật.", "Phòng Đào tạo", "Posted yesterday"));
        list.add(new Announcement("Project Submission Deadline", "Hạn nộp dự án là ngày 15/12.", "Khoa CNTT", "Posted 3 days ago"));
        list.add(new Announcement("Guest Lecture on AI Ethics", "Buổi thuyết giảng về Đạo đức AI vào ngày 15/11.", "Hội Sinh viên", "Posted on Nov 15"));
        list.add(new Announcement("Thông báo khác...", "Nội dung cũ hơn...", "Admin", "Posted last week"));
        return list;
    }
}