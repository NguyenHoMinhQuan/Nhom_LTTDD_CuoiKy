package com.example.client.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.Admin.adapter.CourseAdapter;
import com.example.client.api.AdminResponse;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuanLyKhoaHocActivity extends AppCompatActivity {

    private RecyclerView rcvCourses;
    private CourseAdapter courseAdapter;
    private EditText edtMaMon, edtTenMon, edtTinChi, edtMoTa;
    private AppCompatButton btnSave, btnClear, btnDelete;
    private TextView tvFormTitle;

    private Integer selectedCourseId = null;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_course);

        apiService = ApiClient.getClient(this).create(ApiService.class);

        initViews();
        setupRecyclerView();
        loadCourseData();
    }

    private void initViews() {
        rcvCourses = findViewById(R.id.rcvCourses);
        edtMaMon = findViewById(R.id.edtMaMon);
        edtTenMon = findViewById(R.id.edtTenMon);
        edtTinChi = findViewById(R.id.edtTinChi);
        edtMoTa = findViewById(R.id.edtMoTa);

        btnSave = findViewById(R.id.btnSave);
        btnClear = findViewById(R.id.btnClear);
        btnDelete = findViewById(R.id.btnDelete);
        tvFormTitle = findViewById(R.id.tvFormTitle);

        btnClear.setOnClickListener(v -> resetForm());
        btnSave.setOnClickListener(v -> handleSave());
        btnDelete.setOnClickListener(v -> handleDelete());

        ImageView btnMenu = findViewById(R.id.btnMenu);
        if (btnMenu != null) btnMenu.setOnClickListener(this::showMenu);
    }

    private void setupRecyclerView() {
        courseAdapter = new CourseAdapter(new ArrayList<>(), this::fillFormFromCourse);
        rcvCourses.setLayoutManager(new LinearLayoutManager(this));
        rcvCourses.setAdapter(courseAdapter);
    }
    private void loadCourseData() {
        apiService.getCourses().enqueue(new Callback<List<AdminResponse.CourseRow>>() {
            @Override
            public void onResponse(Call<List<AdminResponse.CourseRow>> call,
                                   Response<List<AdminResponse.CourseRow>> response) {
                if (!isFinishing() && !isDestroyed()
                        && response.isSuccessful()
                        && response.body() != null) {

                    List<AdminResponse.CourseRow> list = response.body();
                    Collections.sort(list, (a, b) -> b.id.compareTo(a.id));
                    courseAdapter.setData(list);
                }
            }

            @Override
            public void onFailure(Call<List<AdminResponse.CourseRow>> call, Throwable t) {
                if (!isFinishing() && !isDestroyed()) {
                    Toast.makeText(QuanLyKhoaHocActivity.this,
                            "Không tải được dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleSave() {
        String maMon = edtMaMon.getText().toString().trim();
        String tenMon = edtTenMon.getText().toString().trim();

        if (maMon.isEmpty() || tenMon.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Mã và Tên môn học!", Toast.LENGTH_SHORT).show();
            return;
        }

        AdminResponse.CourseRequest req = new AdminResponse.CourseRequest();
        req.id = selectedCourseId;
        req.maMon = maMon;
        req.tenMon = tenMon;
        req.tinChi = Integer.parseInt(edtTinChi.getText().toString().isEmpty() ? "0" : edtTinChi.getText().toString());
        req.moTa = edtMoTa.getText().toString().trim();

        if (selectedCourseId == null) {
            executeApiCall(apiService.addCourse(req), "Thêm khóa học thành công!");
        } else {
            executeApiCall(apiService.updateCourse(req), "Cập nhật thành công!");
        }
    }
    private void executeApiCall(Call<ResponseBody> call, String successMsg) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(QuanLyKhoaHocActivity.this, successMsg, Toast.LENGTH_SHORT).show();
                    resetForm();
                    loadCourseData();
                } else {
                    Toast.makeText(
                            QuanLyKhoaHocActivity.this,
                            "Lỗi server (" + response.code() + ")",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(
                        QuanLyKhoaHocActivity.this,
                        "Lỗi mạng / server chưa chạy",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void handleDelete() {
        if (selectedCourseId == null) return;

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc chắn muốn xóa khóa học này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    executeApiCall(apiService.deleteCourse(selectedCourseId), "Đã xóa khóa học!");
                })
                .setNegativeButton("Hủy", null).show();
    }
    // Trong QuanLyKhoaHocActivity.java

    private void fillFormFromCourse(AdminResponse.CourseRow course) {
        // 1. Kiểm tra dữ liệu đầu vào
        if (course == null) return;

        try {
            selectedCourseId = course.id;

            // 2. Gán dữ liệu (Dùng String.valueOf cho số để KHÔNG BỊ VĂNG)
            if (edtMaMon != null) edtMaMon.setText(course.maMon);
            if (edtTenMon != null) edtTenMon.setText(course.tenMon);

            // --- QUAN TRỌNG: Sửa dòng này ---
            if (edtTinChi != null) {
                // Nếu tinChi là null thì hiện số 0, ngược lại hiện giá trị thực
                String strTinChi = String.valueOf(course.tinChi != null ? course.tinChi : 0);
                edtTinChi.setText(strTinChi);
            }

            if (edtMoTa != null) {
                edtMoTa.setText(course.moTa != null ? course.moTa : "");
            }

            // 3. Cập nhật trạng thái nút bấm
            if (btnDelete != null) btnDelete.setVisibility(View.VISIBLE);
            if (btnSave != null) btnSave.setText("CẬP NHẬT");
            if (tvFormTitle != null) tvFormTitle.setText("SỬA KHÓA HỌC: " + course.maMon);

        } catch (Exception e) {
            // Nếu vẫn lỗi, in ra log để biết chính xác tại sao
            e.printStackTrace();
            android.util.Log.e("LOI_APP", "Lỗi fillForm: " + e.getMessage());
            Toast.makeText(this, "Lỗi hiển thị: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void resetForm() {
        selectedCourseId = null;

        if (edtMaMon != null) edtMaMon.setText("");
        if (edtTenMon != null) edtTenMon.setText("");
        if (edtTinChi != null) edtTinChi.setText("3");
        if (edtMoTa != null) edtMoTa.setText("");

        if (btnDelete != null) btnDelete.setVisibility(View.GONE);
        if (btnSave != null) btnSave.setText("LƯU THAY ĐỔI");

        if (tvFormTitle != null) {
            tvFormTitle.setText("THÔNG TIN KHÓA HỌC");
        }
    }
    private void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.nav_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_user) { // Sửa ID đúng để sang trang Lớp học
                startActivity(new Intent(this, QuanLyUserActivity.class));
                return true;
            } else if (id == R.id.menu_lop_hoc) {
                startActivity(new Intent(this, QuanLyLopHocActivity.class));
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