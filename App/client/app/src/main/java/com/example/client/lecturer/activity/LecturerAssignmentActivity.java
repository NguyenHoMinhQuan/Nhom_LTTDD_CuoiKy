package com.example.client.lecturer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
import com.example.client.lecturer.adapter.AssignmentListAdapter;
import com.example.client.lecturer.model.AssignmentDTO;
import com.example.client.lecturer.model.ClassDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LecturerAssignmentActivity extends AppCompatActivity {
    private RecyclerView rvAssignments;
    private Spinner spnFilterClass;
    private ApiService apiService;
    private int lecturerId;

    private List<AssignmentDTO> fullAssignmentList = new ArrayList<>();
    private List<ClassDTO> lecturerClasses = new ArrayList<>();
    private AssignmentListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_assignment_layout);

        // 1. Khởi tạo dữ liệu
        SharedPreferences prefs = getSharedPreferences("AUTH_PREFS", MODE_PRIVATE);
        lecturerId = prefs.getInt("USER_ID", -1);
        apiService = ApiClient.getClient(this).create(ApiService.class);

        // 2. Ánh xạ View
        rvAssignments = findViewById(R.id.rv_assignments);
        spnFilterClass = findViewById(R.id.spn_filter_class);
        rvAssignments.setLayoutManager(new LinearLayoutManager(this));

        // 3. Xử lý sự kiện nút
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.fab_add_assignment).setOnClickListener(v -> {
            // Chuyển sang màn hình AssignmentActivity để tạo bài mới
            startActivity(new Intent(this, LecturerCreateAssignment.class));
        });

        // 4. Luồng xử lý dữ liệu
        loadClassesForFilter();
        fetchAssignments();
    }

    /**
     * Tải danh sách lớp để đổ vào Spinner lọc
     */
    private void loadClassesForFilter() {
        apiService.getClassesByLecturer(lecturerId).enqueue(new Callback<List<ClassDTO>>() {
            @Override
            public void onResponse(Call<List<ClassDTO>> call, Response<List<ClassDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    lecturerClasses = response.body();

                    // Tạo danh sách hiển thị cho Spinner
                    List<String> displayNames = new ArrayList<>();
                    displayNames.add("--- Tất cả các lớp ---"); // Option mặc định
                    for (ClassDTO c : lecturerClasses) {
                        displayNames.add(c.getClassCode() );
                    }

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                            LecturerAssignmentActivity.this,
                            android.R.layout.simple_spinner_item, displayNames);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnFilterClass.setAdapter(spinnerAdapter);

                    // Lắng nghe sự kiện chọn lớp trên Spinner
                    setupSpinnerListener();
                }
            }

            @Override
            public void onFailure(Call<List<ClassDTO>> call, Throwable t) {
                Toast.makeText(LecturerAssignmentActivity.this, "Lỗi tải danh sách lớp", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpinnerListener() {
        spnFilterClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // Hiển thị tất cả
                    updateRecyclerView(fullAssignmentList);
                } else {
                    // Lọc theo ClassId (position - 1 vì đã thêm item "Tất cả")
                    int selectedClassId = lecturerClasses.get(position - 1).getClassId();
                    filterByClass(selectedClassId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**
     * Lấy toàn bộ bài tập của giảng viên từ Server
     */
    private void fetchAssignments() {
        apiService.getAssignmentsByLecturer(lecturerId).enqueue(new Callback<List<AssignmentDTO>>() {
            @Override
            public void onResponse(Call<List<AssignmentDTO>> call, Response<List<AssignmentDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fullAssignmentList = response.body();
                    // Ban đầu hiển thị toàn bộ
                    updateRecyclerView(fullAssignmentList);
                    // Reset spinner về "Tất cả" khi refresh
                    spnFilterClass.setSelection(0);
                }
            }

            @Override
            public void onFailure(Call<List<AssignmentDTO>> call, Throwable t) {
                Toast.makeText(LecturerAssignmentActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void filterByClass(int classId) {
        List<AssignmentDTO> filteredList = new ArrayList<>();
        for (AssignmentDTO item : fullAssignmentList) {
            if (item.getClassId() == classId) {
                filteredList.add(item);
            }
        }
        updateRecyclerView(filteredList);
    }


    private void updateRecyclerView(List<AssignmentDTO> list) {
        adapter = new AssignmentListAdapter(list);
        rvAssignments.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh dữ liệu khi quay lại từ màn hình tạo mới
        fetchAssignments();
    }
}
