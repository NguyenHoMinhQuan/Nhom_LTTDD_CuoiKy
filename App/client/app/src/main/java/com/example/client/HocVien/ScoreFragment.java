package com.example.client.HocVien;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.client.HocVien.Models.HocVien_XemDiemDto;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScoreFragment extends Fragment {

    // Khai báo các view
    private EditText edtUsername, edtClassCode;
    private Button btnViewScore;
    private View layoutResultContainer; // Khung kết quả (để ẩn/hiện)
    private TextView tvStudentName, tvStudentId, tvSubjectName, tvProgressLabel;
    private TableLayout tableScores;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hocvien_layout_score, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Ánh xạ View (Tìm các ID trong layout)
        anhXaView(view);

        // 2. Bắt sự kiện Click nút
        btnViewScore.setOnClickListener(v -> {
            String user = edtUsername.getText().toString().trim();
            String maLop = edtClassCode.getText().toString().trim();

            if (user.isEmpty() || maLop.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                goiApiLayDiem(user, maLop);
            }
        });
    }

    private void anhXaView(View view) {
        edtUsername = view.findViewById(R.id.edtUsername);
        edtClassCode = view.findViewById(R.id.edtClassCode);
        btnViewScore = view.findViewById(R.id.btnViewScore);

        layoutResultContainer = view.findViewById(R.id.layoutResultContainer);
        tvStudentName = view.findViewById(R.id.tvStudentName);
        tvStudentId = view.findViewById(R.id.tvStudentId);
        tvSubjectName = view.findViewById(R.id.tvSubjectName);
        tableScores = view.findViewById(R.id.tableScores);
        tvProgressLabel = view.findViewById(R.id.tvProgressLabel);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void goiApiLayDiem(String username, String classCode) {
        // Tạo API Service
        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);

        // Gọi API
        apiService.xemDiemSinhVien(username, classCode).enqueue(new Callback<List<HocVien_XemDiemDto>>() {
            @Override
            public void onResponse(Call<List<HocVien_XemDiemDto>> call, Response<List<HocVien_XemDiemDto>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Thành công -> Hiển thị dữ liệu
                    hienThiDuLieu(response.body());
                } else {
                    // Thất bại hoặc không có dữ liệu
                    layoutResultContainer.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Không tìm thấy dữ liệu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<HocVien_XemDiemDto>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

    private void hienThiDuLieu(List<HocVien_XemDiemDto> listDiem) {
        // 1. Hiện khung kết quả lên
        layoutResultContainer.setVisibility(View.VISIBLE);

        // 2. Điền thông tin chung (Lấy từ phần tử đầu tiên)
        HocVien_XemDiemDto info = listDiem.get(0);
        tvStudentName.setText(info.getFullName());
        tvStudentId.setText("Mã SV: " + info.getStudentNumber());
        tvSubjectName.setText(info.getCourseName());

        // 3. Xử lý Bảng Điểm (Quan trọng)
        // Xóa hết các dòng cũ, TRỪ dòng tiêu đề (index 0)
        int childCount = tableScores.getChildCount();
        if (childCount > 1) {
            tableScores.removeViews(1, childCount - 1);
        }

        int soBaiDaNop = 0;
        int tongSoBai = listDiem.size();

        // Duyệt qua từng bài tập để tạo dòng mới
        for (HocVien_XemDiemDto item : listDiem) {
            TableRow tableRow = new TableRow(getContext());

            // --- Cột Tên Bài Tập ---
            TextView tvTitle = new TextView(getContext());
            tvTitle.setText(item.getAssignmentTitle());
            tvTitle.setTextColor(Color.BLACK);
            // Padding: trái, trên, phải, dưới (đơn vị pixel, 30px ~ 10dp)
            tvTitle.setPadding(30, 30, 30, 30);
            // Set background để có màu xám xen kẽ như thiết kế cũ
            tvTitle.setBackgroundResource(R.drawable.hocvien_row_table_score_gray);

            // --- Cột Điểm ---
            TextView tvGrade = new TextView(getContext());
            tvGrade.setTextColor(Color.BLACK);
            tvGrade.setPadding(30, 30, 30, 30);
            tvGrade.setGravity(Gravity.CENTER);
            tvGrade.setMinWidth(150);
            tvGrade.setBackgroundResource(R.drawable.hocvien_row_table_score_gray);

            // Logic hiển thị điểm
            if ("Đã nộp".equals(item.getSubmitStatus())) {
                soBaiDaNop++;
                if (item.getGrade() != null) {
                    tvGrade.setText(String.valueOf(item.getGrade()));
                } else {
                    tvGrade.setText("..."); // Đã nộp nhưng chưa chấm
                    tvGrade.setTextColor(Color.BLUE);
                }
            } else {
                tvGrade.setText(""); // Chưa nộp
            }

            // Thêm view vào row, thêm row vào bảng
            tableRow.addView(tvTitle);
            tableRow.addView(tvGrade);
            tableScores.addView(tableRow);
        }

        // 4. Tính % hoàn thành
        int phanTram = 0;
        if (tongSoBai > 0) {
            phanTram = (soBaiDaNop * 100) / tongSoBai;
        }

        tvProgressLabel.setText("Mức độ hoàn thành: " + phanTram + " %");
        progressBar.setProgress(phanTram);
    }
}