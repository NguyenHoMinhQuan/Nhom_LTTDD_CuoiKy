package com.example.client;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class QuanLyLopHocActivity extends AppCompatActivity {

    // Khai báo các View (Nút bấm, Text hiển thị)
    private TextView tabNgay, tabTuan, tabThang, tabNam;
    private TextView btnLui, btnTiep, tvHienThiThoiGian;

    // Biến logic thời gian
    private Calendar calendar;
    private int currentMode = 0; // 0: Ngày, 1: Tuần, 2: Tháng, 3: Năm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_class_calendar);

        // 1. Ánh xạ View (Kết nối Java với XML)
        initViews();

        // 2. Khởi tạo thời gian mặc định là hôm nay
        calendar = Calendar.getInstance();
        updateDisplay(); // Hiển thị ngày hôm nay lên banner đỏ ngay lập tức

        // 3. Xử lý sự kiện bấm vào các Tab
        tabNgay.setOnClickListener(v -> switchMode(0));
        tabTuan.setOnClickListener(v -> switchMode(1));
        tabThang.setOnClickListener(v -> switchMode(2));
        tabNam.setOnClickListener(v -> switchMode(3));

        // 4. Xử lý sự kiện bấm nút Lùi / Tiếp
        btnLui.setOnClickListener(v -> changeTime(-1)); // -1 là lùi
        btnTiep.setOnClickListener(v -> changeTime(1));  // 1 là tiến

        // 5. Các xử lý cũ (Menu 3 gạch & Popup chi tiết môn học) - GIỮ NGUYÊN
        setupOldLogic();
    }

    private void initViews() {
        tabNgay = findViewById(R.id.tabNgay);
        tabTuan = findViewById(R.id.tabTuan);
        tabThang = findViewById(R.id.tabThang);
        tabNam = findViewById(R.id.tabNam);
        btnLui = findViewById(R.id.btnLui);
        btnTiep = findViewById(R.id.btnTiep);
        tvHienThiThoiGian = findViewById(R.id.tvHienThiThoiGian);

        // Mặc định chọn Tab Ngày đầu tiên
        updateTabColor(tabNgay, true);
    }

    // === LOGIC 1: CHUYỂN ĐỔI CHẾ ĐỘ (TAB) ===
    private void switchMode(int mode) {
        this.currentMode = mode;

        // Đổi màu các Tab: Cái nào được chọn thì sáng (Hồng), còn lại tối (Xám)
        updateTabColor(tabNgay, mode == 0);
        updateTabColor(tabTuan, mode == 1);
        updateTabColor(tabThang, mode == 2);
        updateTabColor(tabNam, mode == 3);

        // Cập nhật lại chữ trên banner đỏ cho đúng chế độ mới
        updateDisplay();

        // Hiện bảng chọn (Dialog) để người dùng chọn nhanh
        showPickerDialog(mode);
    }

    // Hàm đổi màu nền Tab (Hồng/Xám)
    private void updateTabColor(TextView tab, boolean isActive) {
        if (isActive) {
            tab.setBackgroundResource(R.drawable.bg_btn_pink);
            tab.setTextColor(Color.BLACK);
        } else {
            tab.setBackgroundResource(R.drawable.bg_btn_grey);
            tab.setTextColor(Color.parseColor("#555555"));
        }
    }

    // === LOGIC 2: HIỆN BẢNG CHỌN (DIALOG) ===
    private void showPickerDialog(int mode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items;

        switch (mode) {
            case 0: // Chọn Ngày (1-31)
                builder.setTitle("Chọn Ngày");
                items = new String[31];
                for(int i=0; i<31; i++) items[i] = "Ngày " + (i+1);

                builder.setItems(items, (dialog, which) -> {
                    calendar.set(Calendar.DAY_OF_MONTH, which + 1);
                    updateDisplay();
                });
                break;

            case 1: // Chọn Tuần (1-4)
                builder.setTitle("Chọn Tuần trong tháng");
                items = new String[]{"Tuần 1", "Tuần 2", "Tuần 3", "Tuần 4"};
                builder.setItems(items, (dialog, which) -> {
                    // Giả lập: Set về ngày đầu tiên của tuần đó
                    calendar.set(Calendar.WEEK_OF_MONTH, which + 1);
                    updateDisplay();
                });
                break;

            case 2: // Chọn Tháng (1-12)
                builder.setTitle("Chọn Tháng");
                items = new String[12];
                for(int i=0; i<12; i++) items[i] = "Tháng " + (i+1);
                builder.setItems(items, (dialog, which) -> {
                    calendar.set(Calendar.MONTH, which); // Tháng trong Java bắt đầu từ 0
                    updateDisplay();
                });
                break;

            case 3: // Chọn Năm (2020 - 2030)
                builder.setTitle("Chọn Năm");
                List<String> years = new ArrayList<>();
                for(int i=2020; i<=2030; i++) years.add("Năm " + i);
                items = years.toArray(new String[0]);

                builder.setItems(items, (dialog, which) -> {
                    calendar.set(Calendar.YEAR, 2020 + which);
                    updateDisplay();
                });
                break;
        }
        builder.show();
    }

    // === LOGIC 3: XỬ LÝ NÚT LÙI / TIẾN ===
    private void changeTime(int amount) {
        switch (currentMode) {
            case 0: // Ngày: Cộng/Trừ 1 ngày
                calendar.add(Calendar.DAY_OF_MONTH, amount);
                break;
            case 1: // Tuần: Cộng/Trừ 1 tuần (7 ngày)
                calendar.add(Calendar.WEEK_OF_YEAR, amount);
                break;
            case 2: // Tháng: Cộng/Trừ 1 tháng
                calendar.add(Calendar.MONTH, amount);
                break;
            case 3: // Năm: Cộng/Trừ 1 năm
                calendar.add(Calendar.YEAR, amount);
                break;
        }
        updateDisplay();
    }

    // === LOGIC 4: HIỂN THỊ TEXT LÊN BANNER ĐỎ ===
    private void updateDisplay() {
        SimpleDateFormat sdf;
        switch (currentMode) {
            case 0: // Chế độ Ngày: "Thứ Hai, 20/11/2025"
                sdf = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("vi", "VN"));
                tvHienThiThoiGian.setText(sdf.format(calendar.getTime()));
                break;
            case 1: // Chế độ Tuần: "Tuần 3 - Tháng 11/2025"
                int week = calendar.get(Calendar.WEEK_OF_MONTH);
                sdf = new SimpleDateFormat("MM/yyyy", Locale.getDefault());
                tvHienThiThoiGian.setText("Tuần " + week + " - Tháng " + sdf.format(calendar.getTime()));
                break;
            case 2: // Chế độ Tháng: "Tháng 11/2025"
                sdf = new SimpleDateFormat("'Tháng' MM/yyyy", new Locale("vi", "VN"));
                tvHienThiThoiGian.setText(sdf.format(calendar.getTime()));
                break;
            case 3: // Chế độ Năm: "Năm 2025"
                sdf = new SimpleDateFormat("'Năm' yyyy", Locale.getDefault());
                tvHienThiThoiGian.setText(sdf.format(calendar.getTime()));
                break;
        }
    }

    // === CÁC LOGIC CŨ (MENU & POPUP) ===
    private void setupOldLogic() {
        ImageView btnMenu = findViewById(R.id.btnMenu);
        if(btnMenu != null) btnMenu.setOnClickListener(v -> showMenu(v));

        // Bắt sự kiện click vào thẻ lớp học 1
        LinearLayout itemLopHoc1 = findViewById(R.id.itemLopHoc1);
        if (itemLopHoc1 != null) itemLopHoc1.setOnClickListener(v -> showDetailPopup());

        // Bắt sự kiện click vào thẻ lớp học 2
        LinearLayout itemLopHoc2 = findViewById(R.id.itemLopHoc2);
        if (itemLopHoc2 != null) itemLopHoc2.setOnClickListener(v -> showDetailPopup());
    }

    private void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.nav_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_user) {
                startActivity(new Intent(this, QuanLyUserActivity.class));
                return true;
            } else if (item.getItemId() == R.id.menu_khoa_hoc) {
                startActivity(new Intent(this, QuanLyKhoaHocActivity.class));
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void showDetailPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.admin_class_detail, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.show();
    }
}