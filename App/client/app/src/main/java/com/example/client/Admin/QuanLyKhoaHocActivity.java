package com.example.client.Admin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.client.R;
import com.example.client.api.AdminResponse;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuanLyKhoaHocActivity extends AppCompatActivity {

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_course);

        // 1. Ánh xạ
        tableLayout = findViewById(R.id.tableCourseLayout);
        ImageView btnMenu = findViewById(R.id.btnMenu);

        // 2. Menu
        if (btnMenu != null) {
            btnMenu.setOnClickListener(this::showMenu);
        }

        // 3. Load API
        loadCourseData();
    }

    private void loadCourseData() {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        apiService.getCourses().enqueue(new Callback<List<AdminResponse.Course>>() {
            @Override
            public void onResponse(Call<List<AdminResponse.Course>> call, Response<List<AdminResponse.Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    renderTable(response.body());
                } else {
                    Toast.makeText(QuanLyKhoaHocActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdminResponse.Course>> call, Throwable t) {
                Toast.makeText(QuanLyKhoaHocActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void renderTable(List<AdminResponse.Course> courseList) {
        // 1. Xóa dữ liệu cũ (Giữ lại dòng tiêu đề ở index 0)
        if (tableLayout.getChildCount() > 1) {
            tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
        }

        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(this);

        for (AdminResponse.Course c : courseList) {
            // 2. Nạp giao diện từ item_row_course.xml
            View rowView = inflater.inflate(R.layout.item_row_course, tableLayout, false);
            TableRow row = (TableRow) rowView;

            // 3. Ánh xạ View
            TextView tvId = row.findViewById(R.id.tvCourseId);
            TextView tvName = row.findViewById(R.id.tvCourseName);
            TextView tvLecturer = row.findViewById(R.id.tvLecturer);
            TextView tvCount = row.findViewById(R.id.tvStudentCount);
            View viewStatus = row.findViewById(R.id.viewStatusColor);

            // 4. Đổ dữ liệu
            tvId.setText(String.valueOf(c.id));
            tvName.setText(c.courseName);
            tvLecturer.setText(c.lecturerName);
            tvCount.setText(String.valueOf(c.studentCount));

            // 5. Xử lý trạng thái: Có học viên (>0) thì Xanh, ngược lại Đỏ
            boolean isOpen = c.studentCount > 0;
            int color = isOpen ? android.graphics.Color.parseColor("#4CAF50") : android.graphics.Color.RED;
            viewStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color));

            // 6. Sự kiện click (Hiệu ứng chọn dòng)
            row.setOnClickListener(v -> {
                for(int i = 1; i < tableLayout.getChildCount(); i++) {
                    tableLayout.getChildAt(i).setBackgroundColor(android.graphics.Color.TRANSPARENT);
                }
                row.setBackgroundColor(android.graphics.Color.parseColor("#E1F5FE"));

                // Toast để test
                // Toast.makeText(QuanLyKhoaHocActivity.this, "Chọn: " + c.courseName, Toast.LENGTH_SHORT).show();
            });

            // 7. Thêm vào bảng
            tableLayout.addView(row);
        }
    }
    // --- Helper (Copy giống User) ---
    private TextView createCell(String text, int widthDp) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundResource(R.drawable.bg_table_cell);
        tv.setPadding(10, 10, 10, 10);
        tv.setTextColor(Color.BLACK);
        int widthPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthDp, getResources().getDisplayMetrics());
        tv.setLayoutParams(new TableRow.LayoutParams(widthPx, TableRow.LayoutParams.MATCH_PARENT));
        return tv;
    }

    private FrameLayout createStatusCell(boolean active) {
        FrameLayout fl = new FrameLayout(this);
        fl.setBackgroundResource(R.drawable.bg_table_cell);
        View circle = new View(this);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);
        params.gravity = Gravity.CENTER;
        circle.setLayoutParams(params);
        circle.setBackgroundResource(R.drawable.bg_btn_pink);
        circle.setBackgroundTintList(android.content.res.ColorStateList.valueOf(active ? Color.parseColor("#4CAF50") : Color.parseColor("#FFC107")));
        fl.addView(circle);
        return fl;
    }

    private void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.nav_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_user) {
                startActivity(new Intent(this, QuanLyUserActivity.class));
                return true;
            } else if (id == R.id.menu_lop_hoc) {
                startActivity(new Intent(this, QuanLyLopHocActivity.class));
                return true;
            }
            return false;
        });
        popup.show();
    }
}