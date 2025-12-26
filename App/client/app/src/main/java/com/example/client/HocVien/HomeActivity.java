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

    private void setupNavigation() {
        tvSearch.setOnClickListener(v -> {
            navigate(SearchActivity.class);
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