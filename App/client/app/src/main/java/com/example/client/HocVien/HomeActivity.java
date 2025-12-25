package com.example.client.HocVien;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.HocVien.Models.ThongBaoModel;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseHocVienActivity {

    private TextView tvSearch;
    private RecyclerView rvAnnouncement; // RecyclerView hiển thị thông báo

    // Adapter và List dữ liệu
    private ThongBaoAdapter announcementAdapter;
    private List<ThongBaoModel> listAnnouncements;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hocvien_dashboard);

        // 1. Setup Header chung
        setupCommonHeader();

        // 2. Ánh xạ View
        // ID trong hocvien_dashboard.xml là recyclerViewCourses
        rvAnnouncement = findViewById(R.id.recyclerViewCourses);
        tvSearch = findViewById(R.id.tvSearchTitle);

        // 3. Xử lý chuyển trang tìm kiếm
        if (tvSearch != null) {
            tvSearch.setOnClickListener(v -> navigate(SearchActivity.class));
        }

        // 4. Cấu hình danh sách và gọi API
        setupAnnouncementList();
    }

    private void setupAnnouncementList() {
        // Khởi tạo list rỗng
        listAnnouncements = new ArrayList<>();

        // Gắn Adapter
        announcementAdapter = new ThongBaoAdapter(listAnnouncements);
        rvAnnouncement.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvAnnouncement.setAdapter(announcementAdapter);

        // Gọi API lấy dữ liệu thật
        getAnnouncementsFromApi();
    }

    private void getAnnouncementsFromApi() {

        //Toast.makeText(this, "Đang gọi API...", Toast.LENGTH_SHORT).show(); //////

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        // Gọi API /api/announcements
        apiService.getListAnnouncements().enqueue(new Callback<List<ThongBaoModel>>() {
            @Override
            public void onResponse(Call<List<ThongBaoModel>> call, Response<List<ThongBaoModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ThongBaoModel> data = response.body();

                    // Xóa dữ liệu cũ và thêm dữ liệu mới
                    listAnnouncements.clear();
                    listAnnouncements.addAll(data);

                    // Cập nhật giao diện
                    announcementAdapter.notifyDataSetChanged();
                } else {
                    // Nếu API trả về rỗng hoặc lỗi
                    Toast.makeText(HomeActivity.this, "Không có thông báo mới", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ThongBaoModel>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}