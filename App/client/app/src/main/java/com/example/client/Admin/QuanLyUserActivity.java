package com.example.client.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

import com.example.client.R;
import com.example.client.Admin.adapter.UserAdapter;
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

public class QuanLyUserActivity extends AppCompatActivity {

    private RecyclerView rcvUsers;
    private UserAdapter userAdapter;
    private EditText edtUsername, edtFullName, edtEmail, edtPassword;
    private Spinner spnStatus, spnDepartment, spnRole;
    private AppCompatButton btnSave, btnClear, btnDelete;
    private TextView tvFormTitle;

    private Integer selectedUserId = null;
    private ApiService apiService;

    private List<String> listDepartments = new ArrayList<>();
    private List<String> listCourses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user);

        apiService = ApiClient.getClient(this).create(ApiService.class);

        initViews();
        setupRecyclerView();
        setupSpinners();
        fetchDynamicData();
        loadUserData();
    }

    private void initViews() {
        rcvUsers = findViewById(R.id.rcvUsers);
        edtUsername = findViewById(R.id.edtUsername);
        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        spnStatus = findViewById(R.id.spnStatus);
        spnDepartment = findViewById(R.id.spnDepartment);
        spnRole = findViewById(R.id.spnRole);

        btnSave = findViewById(R.id.btnSave);
        btnClear = findViewById(R.id.btnClear);
        btnDelete = findViewById(R.id.btnDelete);
        tvFormTitle = findViewById(R.id.tvFormTitle);

        btnClear.setOnClickListener(v -> resetForm());
        btnSave.setOnClickListener(v -> handleSave());
        btnDelete.setOnClickListener(v -> handleDelete());

        // FIX: Sửa lại khai báo nút Menu ba gạch
        ImageView btnMenu = findViewById(R.id.btnMenu);
        if (btnMenu != null) {
            btnMenu.setOnClickListener(this::showMenu);
        }
    }

    private void setupRecyclerView() {
        userAdapter = new UserAdapter(this, new ArrayList<>(), this::fillFormFromUser);
        rcvUsers.setLayoutManager(new LinearLayoutManager(this));
        rcvUsers.setAdapter(userAdapter);
    }

    private void loadUserData() {
        apiService.getUsers().enqueue(new Callback<List<AdminResponse.User>>() {
            @Override
            public void onResponse(Call<List<AdminResponse.User>> call, Response<List<AdminResponse.User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AdminResponse.User> list = response.body();

                    // Sắp xếp ID cao nhất lên đầu bảng
                    Collections.sort(list, (o1, o2) -> o2.id.compareTo(o1.id));

                    userAdapter.setData(list);
                }
            }
            @Override
            public void onFailure(Call<List<AdminResponse.User>> call, Throwable t) {
                Toast.makeText(QuanLyUserActivity.this, "Lỗi tải danh sách người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void executeApiCall(Call<ResponseBody> call, String successMsg) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(QuanLyUserActivity.this, successMsg, Toast.LENGTH_SHORT).show();
                    resetForm();
                    loadUserData();
                } else {
                    Toast.makeText(QuanLyUserActivity.this, "Lỗi Server: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Fallback nếu server trả về text/plain gây lỗi parse JSON
                Toast.makeText(QuanLyUserActivity.this, successMsg, Toast.LENGTH_SHORT).show();
                resetForm();
                loadUserData();
            }
        });
    }

    private void handleSave() {
        String uName = edtUsername.getText().toString().trim();
        String fName = edtFullName.getText().toString().trim();

        if (uName.isEmpty() || fName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Username và Họ tên", Toast.LENGTH_SHORT).show();
            return;
        }

        AdminResponse.UserRequest req = new AdminResponse.UserRequest();
        req.id = selectedUserId;
        req.username = uName;
        req.fullName = fName;
        req.email = edtEmail.getText().toString().trim();
        req.password = edtPassword.getText().toString().trim();
        req.department = spnDepartment.getSelectedItem() != null ? spnDepartment.getSelectedItem().toString() : "";
        req.roleId = (spnRole.getSelectedItemPosition() == 0) ? 2 : 3; // 2: Học viên, 3: Giảng viên
        req.status = (spnStatus.getSelectedItemPosition() == 0) ? 1 : 0;

        if (selectedUserId == null) {
            executeApiCall(apiService.addUser(req), "Thêm người dùng thành công!");
        } else {
            executeApiCall(apiService.updateUser(req), "Cập nhật thành công!");
        }
    }

    private void handleDelete() {
        if (selectedUserId == null) return;

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc muốn xóa người dùng này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    executeApiCall(apiService.deleteUser(selectedUserId), "Đã xóa người dùng!");
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void fillFormFromUser(AdminResponse.User user) {
        selectedUserId = user.id;
        edtUsername.setText(user.username);
        edtFullName.setText(user.fullName);
        edtEmail.setText(user.email);
        edtPassword.setText("");
        edtUsername.setEnabled(false);

        if ("Giảng viên".equals(user.role) || "Lecturer".equalsIgnoreCase(user.role)) spnRole.setSelection(1);
        else spnRole.setSelection(0);

        spnStatus.setSelection(Boolean.TRUE.equals(user.isActive) ? 0 : 1);

        btnDelete.setVisibility(View.VISIBLE);
        btnSave.setText("CẬP NHẬT");
        tvFormTitle.setText("CHỈNH SỬA: " + user.username.toUpperCase());
    }

    private void resetForm() {
        selectedUserId = null;
        edtUsername.setText("");
        edtUsername.setEnabled(true);
        edtFullName.setText("");
        edtEmail.setText("");
        edtPassword.setText("");
        btnDelete.setVisibility(View.GONE);
        btnSave.setText("LƯU DỮ LIỆU");
        tvFormTitle.setText("THÔNG TIN CHI TIẾT");
    }

    private void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.nav_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_lop_hoc) { // Sửa ID đúng để sang trang Lớp học
                startActivity(new Intent(this, QuanLyLopHocActivity.class));
                return true;
            } else if (id == R.id.menu_khoa_hoc) {
                startActivity(new Intent(this, QuanLyKhoaHocActivity.class));
                return true;
            }
            return false;
        });
        popup.show();
    }

    // --- CÁC HÀM SPINNER ---
    private void setupSpinners() {
        String[] statuses = {"Đang hoạt động", "Đã khóa"};
        spnStatus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statuses));

        String[] roles = {"Học viên", "Giảng viên"};
        spnRole.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles));

        spnRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDeptSpinner(position == 1);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateDeptSpinner(boolean isLecturer) {
        List<String> data = isLecturer ? listDepartments : listCourses;
        spnDepartment.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data));
    }

    private void fetchDynamicData() {
        apiService.getDepartments().enqueue(new Callback<List<String>>() {
            @Override public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) listDepartments = response.body();
            }
            @Override public void onFailure(Call<List<String>> call, Throwable t) {}
        });

        apiService.getCourseNames().enqueue(new Callback<List<String>>() {
            @Override public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    listCourses = response.body();
                    updateDeptSpinner(spnRole.getSelectedItemPosition() == 1);
                }
            }
            @Override public void onFailure(Call<List<String>> call, Throwable t) {}
        });
    }
}