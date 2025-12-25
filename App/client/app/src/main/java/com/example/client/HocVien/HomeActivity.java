package com.example.client.HocVien;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.HocVien.Models.ThongBaoModel; // Import Model tiếng Việt
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseHocVienActivity {

    // Khai báo View
    private TextView tvSearch;
    private RecyclerView rvThongBao;

    // Khai báo Adapter và List
    private ThongBaoAdapter thongBaoAdapter;
    private List<ThongBaoModel> listThongBao;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hocvien_dashboard);

        // 1. Setup Header chung (Menu, Avatar...)
        setupCommonHeader();

        // 2. Ánh xạ View
        // Lưu ý: ID trong file xml là recyclerViewCourses
        rvThongBao = findViewById(R.id.recyclerViewCourses);
        tvSearch = findViewById(R.id.tvSearchTitle);

        // 3. Setup sự kiện Click tìm kiếm
        if (tvSearch != null) {
            tvSearch.setOnClickListener(v -> navigate(SearchActivity.class));
        }

        // 4. Cấu hình danh sách và Gọi API
        setupAnnouncementList();
    }

    private void setupAnnouncementList() {
        // Khởi tạo danh sách rỗng
        listThongBao = new ArrayList<>();

        // Khởi tạo Adapter
        thongBaoAdapter = new ThongBaoAdapter(listThongBao);

        // Cấu hình RecyclerView (Dạng danh sách dọc)
        rvThongBao.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvThongBao.setAdapter(thongBaoAdapter);

        // Gọi API lấy dữ liệu thật
        getAnnouncementsFromApi();
    }

    private void getAnnouncementsFromApi() {
        // Tạo API Service
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        // Gọi hàm lấy danh sách (đã định nghĩa trong ApiService)
        apiService.getListAnnouncements().enqueue(new Callback<List<ThongBaoModel>>() {
            @Override
            public void onResponse(Call<List<ThongBaoModel>> call, Response<List<ThongBaoModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ThongBaoModel> data = response.body();

                    // Xóa dữ liệu cũ (nếu có) để tránh trùng lặp
                    listThongBao.clear();

                    // Thêm dữ liệu mới từ Server vào List
                    listThongBao.addAll(data);

                    // Báo cho Adapter biết để vẽ lại màn hình
                    thongBaoAdapter.notifyDataSetChanged();
                } else {
                    // Trường hợp Server trả về lỗi hoặc danh sách rỗng
                    Toast.makeText(HomeActivity.this, "Không có thông báo nào.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ThongBaoModel>> call, Throwable t) {
                // Trường hợp lỗi mạng hoặc Server chưa bật
                Toast.makeText(HomeActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}