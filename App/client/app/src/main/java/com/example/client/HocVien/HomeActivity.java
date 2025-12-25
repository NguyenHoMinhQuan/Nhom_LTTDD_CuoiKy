package com.example.client.HocVien;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseHocVienActivity {
    // Khai báo các thuộc tính
    private ImageView imgAvatar;
    private TextView tvSearch;
    private RecyclerView rvHomeCourses;

    // Khai báo Adapter và List cho phần Mua khóa học

    private List<BuyCourses> featuredList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hocvien_dashboard);

        // Gọi hàm setup header
        setupCommonHeader();

        // Ánh xạ view
        // Đảm bảo ID này trùng khớp với file hocvien_dashboard.xml
        rvHomeCourses = findViewById(R.id.recyclerViewCourses);
        tvSearch = findViewById(R.id.tvSearchTitle);

        setupNavigation();

        // GỌI HÀM TẠO DỮ LIỆU
        setupCourseList();
    }

<<<<<<< HEAD
    private void setupNavigation() {
        tvSearch.setOnClickListener(v -> {
            navigate(SearchActivity.class);
=======
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
>>>>>>> 24bc66f (Đã xong cn thông báo home)
        });
    }

    // Hàm tạo dữ liệu mẫu cho khóa học nổi bật
    private void setupCourseList() {
        featuredList = new ArrayList<>();

        // Lưu ý: Nếu không có ảnh R.drawable.mon3, hãy thay bằng R.drawable.ic_launcher_background
        int imgDefault = R.drawable.ic_launcher_background;

        // Thêm dữ liệu mẫu (Tên, Giá, Thời gian, Ảnh)
        featuredList.add(new BuyCourses("Lập trình Android Pro", "2.499.000 đ", "4 tháng", imgDefault));
        featuredList.add(new BuyCourses("Fullstack Web Developer", "3.199.000 đ", "6 tháng", imgDefault));
        featuredList.add(new BuyCourses("Python cho người mới", "1.200.000 đ", "2 tháng", imgDefault));
        featuredList.add(new BuyCourses("Data Science Cơ bản", "4.500.000 đ", "5 tháng", imgDefault));

        // Khởi tạo Adapter


        // Setup LayoutManager (Dọc)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvHomeCourses.setLayoutManager(linearLayoutManager);

        // Gán Adapter

    }
}