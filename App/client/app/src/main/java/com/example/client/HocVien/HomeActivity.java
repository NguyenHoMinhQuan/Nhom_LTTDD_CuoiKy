package com.example.client.HocVien;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.HocVien.Models.ThongBaoModel; // Import Model
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseHocVienActivity {

    // --- 1. KHAI BÁO BIẾN ---
    private TextView tvSearch;
    private RecyclerView rvThongBao;

    // Adapter và List dữ liệu
    private ThongBaoAdapter thongBaoAdapter;
    private List<ThongBaoModel> listThongBao;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hocvien_dashboard);

        // --- 2. SETUP GIAO DIỆN CHUNG ---
        setupCommonHeader(); //gọi hàm chung

        // --- 3. ÁNH XẠ VIEW ---
        // Đảm bảo ID 'recyclerViewCourses' là đúng trong file xml của bạn
        rvThongBao = findViewById(R.id.recyclerViewCourses);
        tvSearch = findViewById(R.id.tvSearchTitle);

        // --- 4. XỬ LÝ SỰ KIỆN ---
        if (tvSearch != null) {
            tvSearch.setOnClickListener(v -> navigate(SearchActivity.class));
        }

        // --- 5. KHỞI TẠO DANH SÁCH ---
        setupAnnouncementList();
    }

    /**
     * Hàm cấu hình RecyclerView và Adapter
     */
    private void setupAnnouncementList() {
        // Khởi tạo danh sách rỗng để tránh lỗi Null
        listThongBao = new ArrayList<>();

        // Khởi tạo Adapter
        thongBaoAdapter = new ThongBaoAdapter(listThongBao);

        // Cấu hình RecyclerView (Dạng danh sách dọc)
        if (rvThongBao != null) {
            rvThongBao.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            rvThongBao.setAdapter(thongBaoAdapter);
        }

        // Gọi API tải dữ liệu
        getAnnouncementsFromApi();
    }

    /**
     * Hàm gọi API lấy danh sách thông báo
     */
    private void getAnnouncementsFromApi() {
        // Tạo API Service
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        // Gọi API (Tên hàm getListAnnouncements phải khớp với ApiService)
        apiService.getListAnnouncements().enqueue(new Callback<List<ThongBaoModel>>() {
            @Override
            public void onResponse(Call<List<ThongBaoModel>> call, Response<List<ThongBaoModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ThongBaoModel> data = response.body();
                    // sau khi lấy dữ liệu về , gọi adater ra sắp xếp , xử lý , nhét data vào từng dòng của thông báo
                    // Xóa dữ liệu cũ và thêm dữ liệu mới
                    listThongBao.clear();
                    listThongBao.addAll(data);

                    // gọi adapter ,Cập nhật giao diện
                    thongBaoAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(HomeActivity.this, "Không có thông báo mới.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ThongBaoModel>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}