package com.example.client.HocVien.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.HocVien.Adapter.AssignmentAdapter;
import com.example.client.HocVien.HocVien_NopBaiTapActivity;
import com.example.client.HocVien.Models.HocVien_XemDiemDto;
import com.example.client.HocVien.Models.HocVien_NhomLopDto;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignmentFragment extends Fragment {

    private Spinner spChonLop;
    private RecyclerView rvDanhSachBaiTap;

    private AssignmentAdapter adapter;

    private List<HocVien_XemDiemDto> danhSachBaiTap = new ArrayList<>();
    private List<HocVien_NhomLopDto> danhSachLopHoc = new ArrayList<>();

    // Biến lưu mã lớp hiện tại
    private String maLopDangChon = "";
    private String usernameHienTai = "student1";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hocvien_fragment_assignment, container, false);

        spChonLop = view.findViewById(R.id.spChonLop);
        rvDanhSachBaiTap = view.findViewById(R.id.rvDanhSachBaiTap);

        caiDatRecyclerView();

        // Gọi hàm lấy danh sách lớp
        goiApiLayDanhSachLop();

        return view;
    }

    private void caiDatRecyclerView() {
        rvDanhSachBaiTap.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AssignmentAdapter(danhSachBaiTap, baiTapDuocChon -> {
            Intent intent = new Intent(getContext(), HocVien_NopBaiTapActivity.class);
            intent.putExtra("DU_LIEU_BAI_TAP", baiTapDuocChon);
            startActivity(intent);
        });

        rvDanhSachBaiTap.setAdapter(adapter);
    }

    private void goiApiLayDanhSachLop() {
        ApiService api = ApiClient.getClient(getContext()).create(ApiService.class);

        api.LayNhomLopSinhVien(usernameHienTai).enqueue(new Callback<List<HocVien_NhomLopDto>>() {
            @Override
            public void onResponse(Call<List<HocVien_NhomLopDto>> call, Response<List<HocVien_NhomLopDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    danhSachLopHoc = response.body();
                    hienThiSpinnerLopHoc(danhSachLopHoc);
                }
            }

            @Override
            public void onFailure(Call<List<HocVien_NhomLopDto>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải lớp: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hienThiSpinnerLopHoc(List<HocVien_NhomLopDto> danhSachLop) {
        List<String> tenHienThi = new ArrayList<>();
        for (HocVien_NhomLopDto lop : danhSachLop) {
            tenHienThi.add(lop.getCourseName() + " (" + lop.getClassCode() + ")");
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                tenHienThi
        );
        spChonLop.setAdapter(spinnerAdapter);

        spChonLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HocVien_NhomLopDto lopDuocChon = danhSachLop.get(position);

                // --- [CHỖ SỬA 1]: Lưu lại mã lớp vào biến toàn cục ---
                maLopDangChon = lopDuocChon.getClassCode();

                // Gọi hàm lấy bài tập
                goiApiLayDanhSachBaiTap(maLopDangChon);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void goiApiLayDanhSachBaiTap(String maLop) {
        ApiService api = ApiClient.getClient(getContext()).create(ApiService.class);

        api.xemDiemSinhVien(usernameHienTai, maLop).enqueue(new Callback<List<HocVien_XemDiemDto>>() {
            @Override
            public void onResponse(Call<List<HocVien_XemDiemDto>> call, Response<List<HocVien_XemDiemDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    danhSachBaiTap.clear();
                    danhSachBaiTap.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    danhSachBaiTap.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Không có bài tập", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<HocVien_XemDiemDto>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải bài tập", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- [CHỖ SỬA 2]: Thêm hàm onResume vào cuối cùng ---
    // Hàm này tự động chạy khi bạn quay lại từ màn hình Nộp bài
    @Override
    public void onResume() {
        super.onResume();
        // Nếu đã chọn lớp rồi (biến không rỗng), thì load lại dữ liệu mới nhất
        if (!maLopDangChon.isEmpty()) {
            goiApiLayDanhSachBaiTap(maLopDangChon);
        }
    }
}