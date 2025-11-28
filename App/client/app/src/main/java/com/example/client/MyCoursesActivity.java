package com.example.client;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.HocVien.MyCourseAdapter;
import com.example.client.HocVien.MyCourses;

import java.util.ArrayList;
import java.util.List;

public class MyCoursesActivity extends BaseHocVienActivity {

    private ImageView btnBack;
    private RecyclerView rvCourses; // Khai báo RecyclerView
    private MyCourseAdapter adapter;
    private List<MyCourses> listMyCourses;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hocvien_mycourse);

        btnBack = findViewById(R.id.btnBack);

        // 1. Ánh xạ RecyclerView từ layout
        rvCourses = findViewById(R.id.recyclerViewCourses);

        // 2. Setup Header
        setupCommonHeader();

        // 3. Setup Danh sách khóa học (QUAN TRỌNG)
        setupCourseList();

        // 4. Xử lý nút back
        if(btnBack != null){
            btnBack.setOnClickListener(v -> {
                finish();
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
            });
        }
    }

    // Hàm tạo dữ liệu mẫu và cấu hình RecyclerView
    private void setupCourseList() {
        // Khởi tạo danh sách dữ liệu
        listMyCourses = new ArrayList<>();

        // Thêm dữ liệu mẫu giống hình bạn gửi
        // Lưu ý: R.drawable.mon3 là tên ảnh trong hình mẫu của bạn.
        // Nếu project bạn chưa có ảnh tên là 'mon3', hãy đổi thành hình khác (vd: R.drawable.ic_launcher_background) để không bị lỗi.

        String dummyDesc = "Khóa học kéo dài 3 tháng, giúp người học nắm vững kiến thức cơ bản và hành động thực tế.";

        listMyCourses.add(new MyCourses("Lập trình trực quan", dummyDesc, R.drawable.ic_launcher_background));
        listMyCourses.add(new MyCourses("Thiết kế đồ họa", dummyDesc, R.drawable.ic_launcher_background));
        listMyCourses.add(new MyCourses("Lập trình Web", dummyDesc, R.drawable.ic_launcher_background));
        listMyCourses.add(new MyCourses("Cơ sở dữ liệu", dummyDesc, R.drawable.ic_launcher_background));
        listMyCourses.add(new MyCourses("Kỹ năng mềm", dummyDesc, R.drawable.ic_launcher_background));

        // Cấu hình Adapter
        adapter = new MyCourseAdapter(listMyCourses);

        // Cấu hình LayoutManager (Dạng danh sách dọc)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvCourses.setLayoutManager(layoutManager);

        // Gán Adapter vào RecyclerView
        rvCourses.setAdapter(adapter);
    }
}