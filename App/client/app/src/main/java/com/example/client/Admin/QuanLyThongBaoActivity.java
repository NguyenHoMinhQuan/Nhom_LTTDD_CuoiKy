package com.example.client.Admin;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu; // Import Menu
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.Admin.adapter.AnnouncementAdapter;
import com.example.client.R;
import com.example.client.api.AdminResponse;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuanLyThongBaoActivity extends AppCompatActivity {

    private RecyclerView rcvAnnouncement;
    private AnnouncementAdapter adapter;
    private List<AdminResponse.Announcement> mList = new ArrayList<>();
    private ApiService apiService;

    // SỬA: Đổi biến btnBack thành btnMenu
    private ImageView btnMenu;
    private FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_announcement);

        // 1. Khởi tạo API
        apiService = ApiClient.getClient(this).create(ApiService.class);

        // 2. Ánh xạ View
        initViews();

        // 3. Cấu hình RecyclerView
        setupRecyclerView();

        // 4. Load dữ liệu
        loadData();
    }

    private void initViews() {
        rcvAnnouncement = findViewById(R.id.rcvAnnouncement);

        // SỬA: Ánh xạ nút Menu
        btnMenu = findViewById(R.id.btnMenu);
        btnAdd = findViewById(R.id.btnAdd);

        // SỬA: Sự kiện click vào Menu
        btnMenu.setOnClickListener(v -> showMenu(v));

        btnAdd.setOnClickListener(v -> showDialog(null));
    }

    // --- HÀM MỚI: XỬ LÝ MENU DROPDOWN ---
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
            }else if (id == R.id.menu_lop_hoc) {
                startActivity(new Intent(this, QuanLyLopHocActivity.class));
                return true;
            }else if (id == R.id.menu_dashboard) {
                startActivity(new Intent(this, AdminDashboardActivity.class));
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void setupRecyclerView() {
        adapter = new AnnouncementAdapter(mList, new AnnouncementAdapter.IClickListener() {
            @Override
            public void onClick(AdminResponse.Announcement item) {
                showDialog(item);
            }
        });
        rcvAnnouncement.setLayoutManager(new LinearLayoutManager(this));
        rcvAnnouncement.setAdapter(adapter);
    }

    private void loadData() {
        apiService.getAnnouncements().enqueue(new Callback<List<AdminResponse.Announcement>>() {
            @Override
            public void onResponse(Call<List<AdminResponse.Announcement>> call, Response<List<AdminResponse.Announcement>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mList = response.body();
                        if (adapter != null) {
                            adapter.setData(mList);
                        }
                    } else {
                        Toast.makeText(QuanLyThongBaoActivity.this, "Dữ liệu trống", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(QuanLyThongBaoActivity.this, "Lỗi Server: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdminResponse.Announcement>> call, Throwable t) {
                Toast.makeText(QuanLyThongBaoActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void showDialog(AdminResponse.Announcement item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_dialog_announcement, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText etTitle = view.findViewById(R.id.etTitle);
        EditText etBody = view.findViewById(R.id.etBody);
        CheckBox cbIsGlobal = view.findViewById(R.id.cbIsGlobal);
        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnDelete = view.findViewById(R.id.btnDelete);

        if (item != null) {
            etTitle.setText(item.title);
            etBody.setText(item.body);
            cbIsGlobal.setChecked(item.isGlobal != null && item.isGlobal);
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String body = etBody.getText().toString().trim();

            if (title.isEmpty() || body.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            AdminResponse.Announcement req = new AdminResponse.Announcement();
            req.title = title;
            req.body = body;
            req.isGlobal = cbIsGlobal.isChecked();
            req.authorId = "1"; // ID Admin
            req.targetClassId = ""; // Fix lỗi null

            if (item == null) {
                callAddApi(req, dialog);
            } else {
                req.id = item.id;
                callUpdateApi(req, dialog);
            }
        });

        btnDelete.setOnClickListener(v -> {
            if (item != null) callDeleteApi(item.id, dialog);
        });

        dialog.show();
    }

    private void callAddApi(AdminResponse.Announcement req, AlertDialog dialog) {
        apiService.addAnnouncement(req).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(QuanLyThongBaoActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    loadData();
                    dialog.dismiss();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown Error";
                        int errorCode = response.code();
                        android.util.Log.e("API_ERROR", "Code: " + errorCode + " | Body: " + errorBody);
                        Toast.makeText(QuanLyThongBaoActivity.this, "Lỗi (" + errorCode + "): " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(QuanLyThongBaoActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callUpdateApi(AdminResponse.Announcement req, AlertDialog dialog) {
        apiService.updateAnnouncement(req).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(QuanLyThongBaoActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    loadData();
                    dialog.dismiss();
                } else {
                    Toast.makeText(QuanLyThongBaoActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(QuanLyThongBaoActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callDeleteApi(Integer id, AlertDialog dialog) {
        apiService.deleteAnnouncement(id).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(QuanLyThongBaoActivity.this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                    loadData();
                    dialog.dismiss();
                } else {
                    Toast.makeText(QuanLyThongBaoActivity.this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(QuanLyThongBaoActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}