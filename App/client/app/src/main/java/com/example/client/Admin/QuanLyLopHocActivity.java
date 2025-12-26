package com.example.client.Admin;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.Admin.adapter.ClassAdapter;
import com.example.client.R;
import com.example.client.api.AdminResponse;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections; // Dùng để sắp xếp
import java.util.Comparator;  // Dùng để so sánh
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuanLyLopHocActivity extends AppCompatActivity {

    // --- ENUM CHẾ ĐỘ XEM ---
    private static final int MODE_DAY = 0;
    private static final int MODE_WEEK = 1;
    private static final int MODE_MONTH = 2;
    private static final int MODE_YEAR = 3;
    private int currentMode = MODE_DAY; // Mặc định xem theo Ngày

    // --- Biến Giao Diện ---
    private TextView tabNgay, tabTuan, tabThang, tabNam;
    private TextView btnLui, btnTiep, tvHienThiThoiGian;

    // Danh sách
    private RecyclerView rcvListClass;
    private ClassAdapter classAdapter;

    // Hai danh sách: Gốc (từ API) và Hiển thị (sau khi lọc)
    private List<AdminResponse.ClassRow> mListOriginal = new ArrayList<>();
    private List<AdminResponse.ClassRow> mListDisplay = new ArrayList<>();

    // Form Nhập Liệu
    private EditText edtMaMon, edtMaLop, edtGiangVien, edtPhong;
    private TextView tvGioBatDau, tvGioKetThuc;
    private Spinner spnThu;
    private AppCompatButton btnThem, btnLuu, btnXoa, btnHuy;

    // --- Biến Logic ---
    private Calendar calendar;
    private ApiService apiService;
    private Integer selectedScheduleId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_class_calendar);

        // 1. Khởi tạo API & Calendar
        apiService = ApiClient.getClient(this).create(ApiService.class);
        calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY); // Thứ 2 là đầu tuần

        // 2. Ánh xạ & Cấu hình
        initViews();
        setupSpinnerThu();
        setupTimePickers();
        setupActions();
        setupTabActions();

        // 3. Hiển thị ban đầu
        updateTimeDisplay();
        loadClassData();
    }

    private void initViews() {
        // --- Header Tabs ---
        tabNgay = findViewById(R.id.tabNgay);
        tabTuan = findViewById(R.id.tabTuan);
        tabThang = findViewById(R.id.tabThang);
        tabNam = findViewById(R.id.tabNam);

        // --- Navigation ---
        btnLui = findViewById(R.id.btnLui);
        btnTiep = findViewById(R.id.btnTiep);
        tvHienThiThoiGian = findViewById(R.id.tvHienThiThoiGian);
        ImageView btnMenu = findViewById(R.id.btnMenu);
        if (btnMenu != null) btnMenu.setOnClickListener(this::showMenu);

        // --- RecyclerView ---
        rcvListClass = findViewById(R.id.rcvListClass);
        classAdapter = new ClassAdapter(mListDisplay, new ClassAdapter.IClickItemListener() {
            @Override
            public void onClickItem(AdminResponse.ClassRow item) {
                fillForm(item);
                showDetailDialog(item);
            }
        });
        rcvListClass.setLayoutManager(new LinearLayoutManager(this));
        rcvListClass.setAdapter(classAdapter);

        // --- Form nhập liệu ---
        edtMaMon = findViewById(R.id.edtMaMon);
        edtMaLop = findViewById(R.id.edtMaLop);
        edtGiangVien = findViewById(R.id.edtGiangVien);
        edtPhong = findViewById(R.id.edtPhong);
        spnThu = findViewById(R.id.spnThu);
        tvGioBatDau = findViewById(R.id.tvGioBatDau);
        tvGioKetThuc = findViewById(R.id.tvGioKetThuc);

        // --- Buttons ---
        btnThem = findViewById(R.id.btnThem);
        btnLuu = findViewById(R.id.btnLuu);
        btnXoa = findViewById(R.id.btnXoa);
        btnHuy = findViewById(R.id.btnHuy);
    }

    // ==========================================================
    // 1. XỬ LÝ CHUYỂN TAB & GIAO DIỆN
    // ==========================================================
    private void setupTabActions() {
        tabNgay.setOnClickListener(v -> setViewMode(MODE_DAY, tabNgay));
        tabTuan.setOnClickListener(v -> setViewMode(MODE_WEEK, tabTuan));
        tabThang.setOnClickListener(v -> setViewMode(MODE_MONTH, tabThang));
        tabNam.setOnClickListener(v -> setViewMode(MODE_YEAR, tabNam));

        // Mặc định chọn tab Ngày
        setViewMode(MODE_DAY, tabNgay);
    }

    private void setViewMode(int mode, TextView selectedTab) {
        this.currentMode = mode;

        // Reset style
        resetTabStyle(tabNgay);
        resetTabStyle(tabTuan);
        resetTabStyle(tabThang);
        resetTabStyle(tabNam);

        // Highlight tab chọn
        selectedTab.setBackgroundResource(R.drawable.bg_btn_pink);
        selectedTab.setTextColor(Color.BLACK);
        selectedTab.setTypeface(null, android.graphics.Typeface.BOLD);

        updateTimeDisplay();
        filterData();
    }

    private void resetTabStyle(TextView tab) {
        tab.setBackgroundResource(R.drawable.bg_btn_grey);
        tab.setTextColor(Color.parseColor("#555555"));
        tab.setTypeface(null, android.graphics.Typeface.NORMAL);
    }

    // ==========================================================
    // 2. XỬ LÝ LÙI / TIẾP THỜI GIAN
    // ==========================================================
    private void setupActions() {
        btnLui.setOnClickListener(v -> adjustDate(-1));
        btnTiep.setOnClickListener(v -> adjustDate(1));

        btnThem.setOnClickListener(v -> clearForm());
        btnHuy.setOnClickListener(v -> clearForm());
        btnLuu.setOnClickListener(v -> handleSave());
        btnXoa.setOnClickListener(v -> handleDelete());
    }

    private void adjustDate(int amount) {
        switch (currentMode) {
            case MODE_DAY:   calendar.add(Calendar.DAY_OF_YEAR, amount); break;
            case MODE_WEEK:  calendar.add(Calendar.WEEK_OF_YEAR, amount); break;
            case MODE_MONTH: calendar.add(Calendar.MONTH, amount); break;
            case MODE_YEAR:  calendar.add(Calendar.YEAR, amount); break;
        }
        updateTimeDisplay();
        filterData();
    }

    private void updateTimeDisplay() {
        SimpleDateFormat sdf;
        String displayText = "";

        switch (currentMode) {
            case MODE_DAY:
                sdf = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("vi", "VN"));
                displayText = sdf.format(calendar.getTime());
                break;
            case MODE_WEEK:
                Calendar start = (Calendar) calendar.clone();
                start.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                Calendar end = (Calendar) start.clone();
                end.add(Calendar.DAY_OF_YEAR, 6);

                SimpleDateFormat d = new SimpleDateFormat("dd/MM", Locale.getDefault());
                displayText = "Tuần: " + d.format(start.getTime()) + " - " + d.format(end.getTime());
                break;
            case MODE_MONTH:
                sdf = new SimpleDateFormat("'Tháng' MM/yyyy", new Locale("vi", "VN"));
                displayText = sdf.format(calendar.getTime());
                break;
            case MODE_YEAR:
                sdf = new SimpleDateFormat("'Năm' yyyy", new Locale("vi", "VN"));
                displayText = sdf.format(calendar.getTime());
                break;
        }
        if (!displayText.isEmpty()) {
            displayText = displayText.substring(0, 1).toUpperCase() + displayText.substring(1);
        }
        tvHienThiThoiGian.setText(displayText);
    }

    // ==========================================================
    // 3. LOGIC LỌC DỮ LIỆU & SẮP XẾP (QUAN TRỌNG)
    // ==========================================================
    private void loadClassData() {
        apiService.getAdminClasses().enqueue(new Callback<List<AdminResponse.ClassRow>>() {
            @Override
            public void onResponse(Call<List<AdminResponse.ClassRow>> call, Response<List<AdminResponse.ClassRow>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mListOriginal = response.body();
                    filterData();
                } else {
                    Toast.makeText(QuanLyLopHocActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<AdminResponse.ClassRow>> call, Throwable t) {
                Toast.makeText(QuanLyLopHocActivity.this, "Lỗi Server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterData() {
        if (mListOriginal == null || mListOriginal.isEmpty()) return;
        mListDisplay.clear();

        if (currentMode == MODE_DAY) {
            // --- LỌC THEO NGÀY: CHỈ HIỆN ĐÚNG THỨ CỦA NGÀY ĐÓ ---
            int javaDay = calendar.get(Calendar.DAY_OF_WEEK);
            int dbDay = (javaDay == Calendar.SUNDAY) ? 8 : javaDay;

            for (AdminResponse.ClassRow item : mListOriginal) {
                if (item.thu != null && item.thu == dbDay) {
                    mListDisplay.add(item);
                }
            }
            // Sắp xếp theo giờ bắt đầu
            Collections.sort(mListDisplay, (o1, o2) -> {
                String t1 = (o1.gioBD != null) ? o1.gioBD : "";
                String t2 = (o2.gioBD != null) ? o2.gioBD : "";
                return t1.compareTo(t2);
            });

        } else {
            // --- LỌC THEO TUẦN/THÁNG: HIỆN TẤT CẢ NHƯNG SẮP XẾP ---
            mListDisplay.addAll(mListOriginal);

            // Sắp xếp: Thứ 2 -> CN, Sáng -> Chiều
            Collections.sort(mListDisplay, new Comparator<AdminResponse.ClassRow>() {
                @Override
                public int compare(AdminResponse.ClassRow o1, AdminResponse.ClassRow o2) {
                    int thu1 = (o1.thu == null) ? 0 : o1.thu;
                    int thu2 = (o2.thu == null) ? 0 : o2.thu;
                    if (thu1 != thu2) return thu1 - thu2; // So sánh thứ

                    String t1 = (o1.gioBD != null) ? o1.gioBD : "";
                    String t2 = (o2.gioBD != null) ? o2.gioBD : "";
                    return t1.compareTo(t2); // So sánh giờ
                }
            });
        }

        classAdapter.setData(mListDisplay);
    }

    // ==========================================================
    // 4. DIALOG & FORM
    // ==========================================================
    private void showDetailDialog(AdminResponse.ClassRow item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.admin_class_detail, null); // Nhớ đổi tên nếu file XML khác
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        EditText etScheduleId = dialogView.findViewById(R.id.etScheduleId);
        EditText etTenMon = dialogView.findViewById(R.id.etTenMon);
        EditText etMaMon = dialogView.findViewById(R.id.etMaMon);
        EditText etMaLop = dialogView.findViewById(R.id.etMaLop);
        EditText etGiangVien = dialogView.findViewById(R.id.etGiangVien);
        EditText etPhongHoc = dialogView.findViewById(R.id.etPhongHoc);
        EditText etThoiGian = dialogView.findViewById(R.id.etThoiGian);
        AppCompatButton btnClose = dialogView.findViewById(R.id.btnCloseDetail);

        etScheduleId.setText(String.valueOf(item.id));
        etTenMon.setText(item.tenMon != null ? item.tenMon : "");
        etMaMon.setText(item.maMon != null ? item.maMon : "");
        etMaLop.setText(item.maLop != null ? item.maLop : "");
        etGiangVien.setText(item.giangVien != null ? item.giangVien : "");
        etPhongHoc.setText(item.phong != null ? item.phong : "");

        String thuHienThi = (item.thu != null && item.thu == 8) ? "CN" : "Thứ " + (item.thu != null ? item.thu : "?");
        String gioBD = (item.gioBD != null && item.gioBD.length() >= 5) ? item.gioBD.substring(0, 5) : "--:--";
        String gioKT = (item.gioKT != null && item.gioKT.length() >= 5) ? item.gioKT.substring(0, 5) : "--:--";
        etThoiGian.setText(thuHienThi + ": " + gioBD + " - " + gioKT);

        btnClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    // --- Helpers ---
    private void setupSpinnerThu() {
        List<String> days = new ArrayList<>();
        days.add("Thứ Hai"); days.add("Thứ Ba"); days.add("Thứ Tư");
        days.add("Thứ Năm"); days.add("Thứ Sáu"); days.add("Thứ Bảy"); days.add("Chủ Nhật");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnThu.setAdapter(adapter);
    }

    private void setupTimePickers() {
        tvGioBatDau.setOnClickListener(v -> showTimeDialog(tvGioBatDau));
        tvGioKetThuc.setOnClickListener(v -> showTimeDialog(tvGioKetThuc));
    }

    private void showTimeDialog(TextView targetView) {
        Calendar cal = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minuteOfHour) -> {
                    String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minuteOfHour);
                    targetView.setText(time);
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void fillForm(AdminResponse.ClassRow item) {
        selectedScheduleId = item.id;
        edtMaMon.setText(item.maMon);
        edtMaLop.setText(item.maLop);
        edtGiangVien.setText(item.giangVien);
        edtPhong.setText(item.phong);

        if (item.thu != null && item.thu >= 2 && item.thu <= 8) spnThu.setSelection(item.thu - 2);
        else spnThu.setSelection(0);

        String start = (item.gioBD != null && item.gioBD.length() >= 5) ? item.gioBD.substring(0, 5) : "";
        String end = (item.gioKT != null && item.gioKT.length() >= 5) ? item.gioKT.substring(0, 5) : "";
        tvGioBatDau.setText(start); tvGioKetThuc.setText(end);

        btnLuu.setText("Cập nhật");
        btnXoa.setVisibility(View.VISIBLE);
    }

    private void clearForm() {
        selectedScheduleId = null;
        edtMaMon.setText(""); edtMaLop.setText(""); edtGiangVien.setText(""); edtPhong.setText("");
        tvGioBatDau.setText(""); tvGioKetThuc.setText("");
        spnThu.setSelection(0);
        btnLuu.setText("Lưu"); btnXoa.setVisibility(View.GONE);
    }

    // --- API CRUD ---
    private void handleSave() {
        AdminResponse.ClassRequest req = new AdminResponse.ClassRequest();
        req.courseCode = edtMaMon.getText().toString().trim();
        req.classCode = edtMaLop.getText().toString().trim();
        req.lecturerName = edtGiangVien.getText().toString().trim();
        req.room = edtPhong.getText().toString().trim();
        req.dayOfWeek = spnThu.getSelectedItemPosition() + 2;
        req.startTime = tvGioBatDau.getText().toString().trim();
        req.endTime = tvGioKetThuc.getText().toString().trim();

        if (req.classCode.isEmpty() || req.startTime.isEmpty() || req.endTime.isEmpty()) {
            Toast.makeText(this, "Thiếu thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedScheduleId == null) { // ADD
            apiService.addClass(req).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(QuanLyLopHocActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        loadClassData(); clearForm();
                    } else Toast.makeText(QuanLyLopHocActivity.this, "Lỗi thêm mới", Toast.LENGTH_SHORT).show();
                }
                @Override public void onFailure(Call<Map<String, Object>> call, Throwable t) {}
            });
        } else { // UPDATE
            req.scheduleId = selectedScheduleId;
            apiService.updateClass(req).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(QuanLyLopHocActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        loadClassData(); clearForm();
                    } else Toast.makeText(QuanLyLopHocActivity.this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
                }
                @Override public void onFailure(Call<Map<String, Object>> call, Throwable t) {}
            });
        }
    }

    private void handleDelete() {
        if (selectedScheduleId == null) return;
        apiService.deleteClass(selectedScheduleId).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(QuanLyLopHocActivity.this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                    loadClassData(); clearForm();
                } else Toast.makeText(QuanLyLopHocActivity.this, "Lỗi xóa", Toast.LENGTH_SHORT).show();
            }
            @Override public void onFailure(Call<Map<String, Object>> call, Throwable t) {}
        });
    }
    private void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.nav_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_user) { // Sửa ID đúng để sang trang Lớp học
                startActivity(new Intent(this, QuanLyUserActivity.class));
                return true;
            } else if (id == R.id.menu_khoa_hoc) {
                startActivity(new Intent(this, QuanLyKhoaHocActivity.class));
                return true;
            }else if (id == R.id.menu_thong_bao) {
                startActivity(new Intent(this, QuanLyThongBaoActivity.class));
                return true;
            }else if (id == R.id.menu_dashboard) {
                startActivity(new Intent(this, AdminDashboardActivity.class));
                return true;
            }
            return false;
        });
        popup.show();
    }
}