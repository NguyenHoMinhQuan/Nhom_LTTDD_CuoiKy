package com.example.client.Admin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
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

public class QuanLyUserActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    // Khai báo các biến nhập liệu (nếu bạn đã thêm ID vào XML)
    // private EditText etHoTen, etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user);

        // 1. Ánh xạ View
        tableLayout = findViewById(R.id.tableUserLayout);
        ImageView btnMenu = findViewById(R.id.btnMenu);

        // Ánh xạ các trường nhập liệu (Bạn cần thêm ID vào XML admin_user.xml để dòng này hoạt động)
        // etHoTen = findViewById(R.id.etHoTen); // Ví dụ

        // 2. Xử lý Menu
        if (btnMenu != null) {
            btnMenu.setOnClickListener(this::showMenu);
        }

        // 3. Gọi API lấy dữ liệu
        loadUserData();
    }

    private void loadUserData() {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        apiService.getUsers().enqueue(new Callback<List<AdminResponse.User>>() {
            @Override
            public void onResponse(Call<List<AdminResponse.User>> call, Response<List<AdminResponse.User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    renderTable(response.body());
                } else {
                    Toast.makeText(QuanLyUserActivity.this, "Không lấy được dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdminResponse.User>> call, Throwable t) {
                Toast.makeText(QuanLyUserActivity.this, "Lỗi kết nối Server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void renderTable(List<AdminResponse.User> userList) {
        // 1. Xóa các dòng cũ (Giữ lại dòng tiêu đề ở vị trí 0)
        if (tableLayout.getChildCount() > 1) {
            tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
        }

        // Chuẩn bị LayoutInflater
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(this);

        for (AdminResponse.User user : userList) {
            // 2. Nạp (Inflate) file giao diện item_row_user.xml
            // Tham số 'false' nghĩa là chưa add ngay vào table, ta sẽ add sau khi điền dữ liệu
            View rowView = inflater.inflate(R.layout.item_row_user, tableLayout, false);
            TableRow row = (TableRow) rowView;

            // 3. Ánh xạ các view trong dòng vừa tạo
            TextView tvId = row.findViewById(R.id.tvId);
            TextView tvName = row.findViewById(R.id.tvName);
            TextView tvEmail = row.findViewById(R.id.tvEmail);
            TextView tvDept = row.findViewById(R.id.tvDept);
            View viewStatus = row.findViewById(R.id.viewStatusColor);

            // 4. Đổ dữ liệu
            tvId.setText(String.valueOf(user.id));
            tvName.setText(user.fullName != null ? user.fullName : "---");
            tvEmail.setText(user.email);
            tvDept.setText(user.department);

            // 5. Xử lý màu trạng thái
            boolean isActive = "Hoạt động".equals(user.status);
            int color = isActive ? android.graphics.Color.parseColor("#4CAF50") : android.graphics.Color.RED;
            viewStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color));

            // 6. Sự kiện click vào dòng (Hiệu ứng đổi màu nền)
            row.setOnClickListener(v -> {
                // Reset màu nền các dòng khác về trong suốt
                for(int i = 1; i < tableLayout.getChildCount(); i++) {
                    tableLayout.getChildAt(i).setBackgroundColor(android.graphics.Color.TRANSPARENT);
                }
                // Đặt màu xanh nhạt cho dòng đang chọn
                row.setBackgroundColor(android.graphics.Color.parseColor("#E1F5FE"));

                // Toast tên người dùng để kiểm tra
                // Toast.makeText(QuanLyUserActivity.this, "Chọn: " + user.fullName, Toast.LENGTH_SHORT).show();
            });

            // 7. Thêm dòng đã hoàn thiện vào bảng
            tableLayout.addView(row);
        }
    }
    // --- CÁC HÀM TẠO GIAO DIỆN (Helper) ---
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

    private FrameLayout createStatusCell(boolean isActive) {
        FrameLayout fl = new FrameLayout(this);
        fl.setBackgroundResource(R.drawable.bg_table_cell);

        View circle = new View(this);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);
        params.gravity = Gravity.CENTER;
        circle.setLayoutParams(params);

        circle.setBackgroundResource(R.drawable.bg_btn_pink);
        // Xanh lá nếu Active, Đỏ nếu Inactive
        circle.setBackgroundTintList(android.content.res.ColorStateList.valueOf(isActive ? Color.parseColor("#4CAF50") : Color.RED));

        fl.addView(circle);
        return fl;
    }

    private void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.nav_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_khoa_hoc) {
                startActivity(new Intent(this, QuanLyKhoaHocActivity.class));
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