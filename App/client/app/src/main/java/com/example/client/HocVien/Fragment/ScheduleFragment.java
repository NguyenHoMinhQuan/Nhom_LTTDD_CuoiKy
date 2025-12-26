package com.example.client.HocVien.Fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client.HocVien.Models.LichHocSinhVienModel;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ScheduleFragment extends Fragment {
    private TextInputEditText edtTimKiemUsername ;
    private Button btnXemLich;
    private LinearLayout layoutLichHocContainer;
    private TextView tabT2, tabT3, tabT4, tabT5, tabT6, tabT7, tabCN;

    // Biến lưu dữ liệu toàn bộ lịch học (để lọc sau này)
    private List<LichHocSinhVienModel> dsLichHoc = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Liên kết với layout cũ hocvien_layout_calender_school
        return inflater.inflate(R.layout.hocvien_layout_calender_school, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        if (btnXemLich == null) {
            Log.e("Loi", "Khong tim thay nut btnXemLich trong XML");
            return; // Dừng lại để không bị crash
        }
        btnXemLich.setOnClickListener(v ->{
            String username = edtTimKiemUsername.getText().toString().trim();
            if (username.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập Username!", Toast.LENGTH_SHORT).show();
                return;
            }
            layLichHocTuServer(username);
        });
        setupTabEvents();
    }
    private void initView(View view){
        edtTimKiemUsername = view.findViewById(R.id.edtSearchUsername);
        btnXemLich = view.findViewById(R.id.btnXemLich);
        layoutLichHocContainer = view.findViewById(R.id.layoutLichHocContainer);

        tabT2 = view.findViewById(R.id.tabT2);
        tabT3 = view.findViewById(R.id.tabT3);
        tabT4 = view.findViewById(R.id.tabT4);
        tabT5 = view.findViewById(R.id.tabT5);
        tabT6 = view.findViewById(R.id.tabT6);
        tabT7 = view.findViewById(R.id.tabT7);
        tabCN = view.findViewById(R.id.tabCN);
    }
    // --- LOGIC GỌI API ---
    private void layLichHocTuServer(String username) {
        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);

        // Gọi API lấy danh sách
        apiService.LayLichHocSinhVien(username).enqueue(new Callback<List<LichHocSinhVienModel>>() {
            @Override
            public void onResponse(Call<List<LichHocSinhVienModel>> call, Response<List<LichHocSinhVienModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Lưu dữ liệu vào biến toàn cục
                    dsLichHoc = response.body();

                    Toast.makeText(getContext(), "Đã tải: " + dsLichHoc.size() + " môn học", Toast.LENGTH_SHORT).show();

                    // Mặc định hiển thị Thứ 2 (DayOfWeek = 1 theo quy ước của bạn)
                    hienThiLichTheoThu(1, tabT2);
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy lịch học!", Toast.LENGTH_SHORT).show();
                    dsLichHoc.clear(); // Xóa dữ liệu cũ nếu lỗi
                    layoutLichHocContainer.removeAllViews();
                }
            }

            @Override
            public void onFailure(Call<List<LichHocSinhVienModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void hienThiLichTheoThu(int dayOfWeekCanLoc, TextView selectedTab) {

        // A. Đổi màu Tab (Reset tất cả về xám, chỉ tab chọn là đỏ)
        resetTabStyle();
        highlightTab(selectedTab);

        // B. Xóa sạch giao diện cũ trong container
        layoutLichHocContainer.removeAllViews();

        // C. Lọc và Vẽ thẻ mới
        boolean coLichHoc = false;

        for (LichHocSinhVienModel monHoc : dsLichHoc) {
            // Kiểm tra xem môn này có học vào thứ đang chọn không?
            if (monHoc.getDayOfWeek() == dayOfWeekCanLoc) {
                coLichHoc = true;

                // 1. Tạo view từ file item_schedule_card.xml
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.hocvien_item_lichhoc, layoutLichHocContainer, false);

                // 2. Ánh xạ các chữ trong thẻ đó
                TextView tvMon = itemView.findViewById(R.id.tvMonHoc);
                TextView tvGio = itemView.findViewById(R.id.tvThoiGian);
                TextView tvPhong = itemView.findViewById(R.id.tvPhongHoc);
                TextView tvGV = itemView.findViewById(R.id.tvGiangVien);

                // 3. Điền dữ liệu
                tvMon.setText(monHoc.getCourseName());
                // Cắt chuỗi giờ cho đẹp (Ví dụ 07:00:00 -> 07:00)
                String start = monHoc.getStartTime().length() >= 5 ? monHoc.getStartTime().substring(0, 5) : monHoc.getStartTime();
                String end = monHoc.getEndTime().length() >= 5 ? monHoc.getEndTime().substring(0, 5) : monHoc.getEndTime();

                tvGio.setText(start + " - " + end);
                tvPhong.setText("Phòng: " + monHoc.getRoom());
                tvGV.setText("Lớp: " + monHoc.getClassCode());

                // 4. Thêm vào container
                layoutLichHocContainer.addView(itemView);
            }
        }

        // Nếu ngày đó không có môn nào
        if (!coLichHoc) {
            TextView tvThongBao = new TextView(getContext());
            tvThongBao.setText("Không có lịch học vào ngày này.");
            tvThongBao.setPadding(20, 20, 20, 20);
            tvThongBao.setTextColor(Color.GRAY);
            tvThongBao.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            layoutLichHocContainer.addView(tvThongBao);
        }
    }
    private void setupTabEvents() {
        // Gán sự kiện cho từng nút (Map số 1->7 theo quy ước json của bạn)
        // 1: Thứ 2, 2: Thứ 3...
        tabT2.setOnClickListener(v -> hienThiLichTheoThu(1, tabT2));
        tabT3.setOnClickListener(v -> hienThiLichTheoThu(2, tabT3));
        tabT4.setOnClickListener(v -> hienThiLichTheoThu(3, tabT4));
        tabT5.setOnClickListener(v -> hienThiLichTheoThu(4, tabT5));
        tabT6.setOnClickListener(v -> hienThiLichTheoThu(5, tabT6));
        tabT7.setOnClickListener(v -> hienThiLichTheoThu(6, tabT7));
        tabCN.setOnClickListener(v -> hienThiLichTheoThu(7, tabCN));
    }

    private void resetTabStyle() {
        // Đưa tất cả các tab về màu xám
        TextView[] allTabs = {tabT2, tabT3, tabT4, tabT5, tabT6, tabT7, tabCN};
        for (TextView tab : allTabs) {
            tab.setBackgroundResource(R.drawable.bg_btn_grey); // File nền xám
            tab.setTextColor(Color.parseColor("#555555")); // Màu chữ xám đen
        }
    }

    private void highlightTab(TextView tab) {
        // Đưa tab được chọn về màu đỏ/hồng
        tab.setBackgroundResource(R.drawable.bg_btn_pink); // File nền đỏ
        tab.setTextColor(Color.RED); // Màu chữ đỏ
    }
}