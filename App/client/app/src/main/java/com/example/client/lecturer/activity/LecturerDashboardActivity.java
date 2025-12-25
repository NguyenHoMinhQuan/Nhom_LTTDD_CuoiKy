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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client.Login.LoginActivity;
import com.example.client.lecturer.activity.ProfileActivity;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
import com.example.client.lecturer.adapter.NotificationAdapter;
import com.example.client.lecturer.adapter.ScheduleAdapter;
import com.example.client.lecturer.model.NotificationItem;
import com.example.client.lecturer.model.ScheduleItem;

import java.util.List;

public class LecturerDashboardActivity extends AppCompatActivity
        implements ScheduleAdapter.OnItemClickListener, NotificationAdapter.OnItemClickListener {

    private RecyclerView timetableRecyclerView;
    private RecyclerView announcementRecyclerView;
    private ImageView ivMessenger;
    private TextView tvViewAll;
    private NotificationAdapter notificationAdapter;
    private ApiService apiService;
    private  ImageView ivAvatar;
    private int currentUserId;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_dashboard);

        // 🟢 BƯỚC 1: LẤY DỮ LIỆU TỪ SHAREDPREFERENCES
        SharedPreferences prefs = getSharedPreferences("AUTH_PREFS", MODE_PRIVATE);
        currentUserId = prefs.getInt("USER_ID", -1); // Lấy ID, mặc định là -1 nếu không có
        currentUsername = prefs.getString("USERNAME", "Giảng viên");

        // Kiểm tra nếu chưa đăng nhập (ID = -1) thì đá về Login ngay
        if (currentUserId == -1) {
            Toast.makeText(this, "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        timetableRecyclerView = findViewById(R.id.recycler_timetable_today);
        announcementRecyclerView = findViewById(R.id.recycler_announcements_recent);
        ivMessenger = findViewById(R.id.iv_messenger);
        tvViewAll = findViewById(R.id.tv_view_all);

        ivAvatar = findViewById(R.id.iv_avatar);

        ivAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(LecturerDashboardActivity.this, ProfileActivity.class);
            startActivity(intent);
        });


        tvViewAll.setOnClickListener(v -> {
            startActivity(new Intent(this, NotificationActivity.class));
        });

        initRetrofit();
        setupQuickActions();
        // 🟢 BƯỚC 2: GỌI API VỚI ID THỰC TẾ
        fetchTodayLecturerSchedule(currentUserId);
        fetchUnreadNotifications(currentUserId);
        setupHeader();


        ivMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LecturerDashboardActivity.this, ChatListActivity.class));
            }
        });
    }


    private void initRetrofit() {
        apiService = ApiClient.getClient(this).create(ApiService.class);
    }

    private void fetchTodayLecturerSchedule(Integer lecturerId) {
        apiService.getTodayScheduleByLecturerId(lecturerId).enqueue(new Callback<List<ScheduleItem>>() {
            @Override
            public void onResponse(Call<List<ScheduleItem>> call, Response<List<ScheduleItem>> response) {
                if (response.isSuccessful() && response.body()!= null) {
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

        // 🟢 BƯỚC 3: HIỂN THỊ TÊN LẤY TỪ LOGIN
        // Lưu ý: Đảm bảo trong strings.xml có dòng: <string name="greeting_format">Xin chào, %s</string>
        // Nếu không có resource thì dùng: greetingTv.setText("Xin chào, " + currentUsername);

        try {
            greetingTv.setText(getString(R.string.greeting_format, currentUsername));
        } catch (Exception e) {
            greetingTv.setText("Hello, " + currentUsername);
        }
    }

    /** Thiết lập Nút Hành động Nhanh */
    private void setupQuickActions() {
        // --- 1. Announcements ---
        View anncAction = findViewById(R.id.action_announcements);
        ImageView anncIcon = anncAction.findViewById(R.id.iv_action_icon);
        TextView anncText = anncAction.findViewById(R.id.tv_action_text);

        anncIcon.setImageResource(R.drawable.announcement);
        anncText.setText("Đăng thông báo");
        anncAction.setOnClickListener(v -> {
            // Mở màn hình danh sách thông báo
            startActivity(new Intent(this, AnnouncementActivity.class));
        });

        // --- 2. Timetable ---
        View ttAction = findViewById(R.id.action_timetable);
        ImageView ttIcon = ttAction.findViewById(R.id.iv_action_icon);
        TextView ttText = ttAction.findViewById(R.id.tv_action_text);

        ttIcon.setImageResource(R.drawable.timetable); // Thay bằng icon thực tế
        ttText.setText("Thời khóa biểu");
        ttAction.setOnClickListener(v -> {
            // Mở màn hình thời khóa biểu đầy đủ
            startActivity(new Intent(this, TimetableActivity.class));
        });

        // --- 3. Assign Homework ---
        View hwAction = findViewById(R.id.action_assign_homework);
        ImageView hwIcon = hwAction.findViewById(R.id.iv_action_icon);
        TextView hwText = hwAction.findViewById(R.id.tv_action_text);

        hwIcon.setImageResource(R.drawable.assign); // Thay bằng icon thực tế
        hwText.setText("Giao bài tập");
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

    private void fetchUnreadNotifications(Integer userId) {
        // Gọi API lấy thông báo chưa đọc từ ApiService mà chúng ta đã thêm trước đó
        apiService.getUnreadNotifications(userId).enqueue(new Callback<List<NotificationItem>>() {
            @Override
            public void onResponse(Call<List<NotificationItem>> call, Response<List<NotificationItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NotificationItem> notifications = response.body();

                    // Hiển thị danh sách lên RecyclerView
                    announcementRecyclerView.setLayoutManager(new LinearLayoutManager(LecturerDashboardActivity.this));

                    // Adapter mới nhận NotificationItem và xử lý click
                    notificationAdapter = new NotificationAdapter(notifications, item -> {
                        // 1. Đánh dấu đã đọc trên Backend
                        markAsReadOnServer(item.getNotificationId());

                        // 2. Mở màn hình chi tiết (tùy chọn)
                        Intent intent = new Intent(LecturerDashboardActivity.this, NotificationDetailActivity.class);
                        intent.putExtra("NOTIFICATION_DATA", item);
                        startActivity(intent);
                    });

                    announcementRecyclerView.setAdapter(notificationAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<NotificationItem>> call, Throwable t) {
                Toast.makeText(LecturerDashboardActivity.this, "Lỗi tải thông báo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm phụ để gọi API Mark as Read
    private void markAsReadOnServer(Integer notificationId) {
        apiService.markAsRead(notificationId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Sau khi đánh dấu thành công, có thể gọi lại fetchUnreadNotifications để refresh dashboard
                fetchUnreadNotifications(currentUserId);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
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
    public void onItemClick(NotificationItem item) {
        // 1. Đánh dấu đã đọc trên Server
        markAsReadOnServer(item.getNotificationId());

        // 2. Mở chi tiết (Nếu cần)
        Toast.makeText(this, "Đang đọc: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, AnnouncementDetailActivity.class);
        // intent.putExtra("NOTIFICATION_DATA", item);
        // startActivity(intent);
    }
}