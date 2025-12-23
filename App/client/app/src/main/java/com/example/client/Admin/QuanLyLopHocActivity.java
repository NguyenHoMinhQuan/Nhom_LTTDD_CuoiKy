package com.example.client.Admin;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.client.R;
import com.example.client.api.AdminResponse;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuanLyLopHocActivity extends AppCompatActivity {

    private TextView tabNgay, btnLui, btnTiep, tvHienThiThoiGian;
    private LinearLayout containerSchedule;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_class_calendar);

        initViews();
        calendar = Calendar.getInstance();
        updateTimeDisplay();

        ImageView btnMenu = findViewById(R.id.btnMenu);
        if(btnMenu != null) btnMenu.setOnClickListener(this::showMenu);

        loadClassData();
        setupTimeEvents();
    }

    private void initViews() {
        tabNgay = findViewById(R.id.tabNgay);
        btnLui = findViewById(R.id.btnLui);
        btnTiep = findViewById(R.id.btnTiep);
        tvHienThiThoiGian = findViewById(R.id.tvHienThiThoiGian);

        // Tìm đúng ID containerListClass đã thêm trong XML
        containerSchedule = findViewById(R.id.containerListClass);
    }

    private void loadClassData() {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        apiService.getClasses().enqueue(new Callback<List<AdminResponse.ClassItem>>() {
            @Override
            public void onResponse(Call<List<AdminResponse.ClassItem>> call, Response<List<AdminResponse.ClassItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    renderScheduleList(response.body());
                } else {
                    Toast.makeText(QuanLyLopHocActivity.this, "Không có lịch học nào", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdminResponse.ClassItem>> call, Throwable t) {
                Toast.makeText(QuanLyLopHocActivity.this, "Lỗi kết nối Server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void renderScheduleList(List<AdminResponse.ClassItem> list) {
        // Xóa sạch view cũ trước khi thêm mới
        if (containerSchedule != null) {
            containerSchedule.removeAllViews();
        }

        for (AdminResponse.ClassItem item : list) {
            // Đảm bảo bạn đã tạo file item_class_schedule_layout.xml
            View itemView = getLayoutInflater().inflate(R.layout.item_class_schedule_layout, containerSchedule, false);

            TextView tvTenMon = itemView.findViewById(R.id.tvTenMon);
            TextView tvThoiGian = itemView.findViewById(R.id.tvThoiGian);
            TextView tvPhong = itemView.findViewById(R.id.tvPhong);
            TextView tvLop = itemView.findViewById(R.id.tvLop);

            tvTenMon.setText(item.courseName);
            tvThoiGian.setText("🕒 " + item.timeRange);
            tvPhong.setText("Phòng: " + item.room);
            tvLop.setText("Lớp: " + item.classCode);

            itemView.setOnClickListener(v -> showDetailPopup(item));

            containerSchedule.addView(itemView);
        }
    }

    private void showDetailPopup(AdminResponse.ClassItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Đảm bảo file layout popup tên là admin_class_detail.xml
        View dialogView = getLayoutInflater().inflate(R.layout.admin_class_detail, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        EditText etTenLop = dialogView.findViewById(R.id.etTenLop);
        EditText etGiangVien = dialogView.findViewById(R.id.etGiangVien);
        EditText etKhoaHoc = dialogView.findViewById(R.id.etKhoaHoc);
        EditText etPhong = dialogView.findViewById(R.id.etPhongHoc);
        EditText etThoiGian = dialogView.findViewById(R.id.etThoiGian);

        // Đổ dữ liệu
        if(etTenLop != null) etTenLop.setText(item.classCode);
        if(etKhoaHoc != null) etKhoaHoc.setText(item.courseName);
        if(etPhong != null) etPhong.setText(item.room);
        if(etThoiGian != null) etThoiGian.setText(item.timeRange);

        // Lưu ý: API ClassItem hiện tại chưa có tên giảng viên (lecturerName),
        // nếu muốn hiện phải thêm vào DTO backend. Hiện tại để trống hoặc setText("")
        if(etGiangVien != null) etGiangVien.setText("");

        if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void updateTimeDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("vi", "VN"));
        tvHienThiThoiGian.setText(sdf.format(calendar.getTime()));
    }

    private void setupTimeEvents() {
        btnLui.setOnClickListener(v -> {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            updateTimeDisplay();
        });
        btnTiep.setOnClickListener(v -> {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            updateTimeDisplay();
        });
    }

    private void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.nav_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_user) {
                startActivity(new Intent(this, QuanLyUserActivity.class));
                return true;
            } else if (id == R.id.menu_khoa_hoc) {
                startActivity(new Intent(this, QuanLyKhoaHocActivity.class));
                return true;
            }
            return false;
        });
        popup.show();
    }
}