package com.example.client.lecturer.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
import com.example.client.lecturer.model.AssignmentDTO;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity hiển thị chi tiết bài tập, cho phép chỉnh sửa hoặc xóa.
 * Đã sửa lỗi chính tả getAssignmentId và logic cập nhật ngày tháng.
 */
public class LecturerAssignmentDetailActivity extends AppCompatActivity {

    private EditText edtTitle, edtDescription, edtDueDate;
    private Button btnUpdate, btnDelete;
    private ApiService apiService;
    private AssignmentDTO currentAssignment;
    private int lecturerId;

    private SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_assignment_detail);

        // 1. Lấy lecturerId để đảm bảo khi update không bị mất thông tin người sở hữu
        SharedPreferences prefs = getSharedPreferences("AUTH_PREFS", MODE_PRIVATE);
        lecturerId = prefs.getInt("USER_ID", -1);

        // 2. Nhận dữ liệu từ Intent
        currentAssignment = (AssignmentDTO) getIntent().getSerializableExtra("ASSIGNMENT_DATA");

        if (currentAssignment == null) {
            Toast.makeText(this, "Không tìm thấy dữ liệu bài tập!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 3. Ánh xạ View
        edtTitle = findViewById(R.id.edt_title);
        edtDescription = findViewById(R.id.edt_description);
        edtDueDate = findViewById(R.id.edt_due_date);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);
        apiService = ApiClient.getClient(this).create(ApiService.class);

        // 4. Đổ dữ liệu vào View
        edtTitle.setText(currentAssignment.getTitle());
        edtDescription.setText(currentAssignment.getDescription());

        String rawDate = currentAssignment.getDueDate();
        if (rawDate != null) {
            // Hiển thị ngày đơn giản: lấy phần trước ký tự 'T'
            String dateOnly = rawDate.contains("T") ? rawDate.split("T")[0] : rawDate;
            edtDueDate.setText(dateOnly);
            edtDueDate.setTag(rawDate); // Giữ nguyên tag định dạng server ISO cho việc cập nhật sau này
        }

        // 5. Sự kiện
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        edtDueDate.setOnClickListener(v -> showDatePicker());
        btnUpdate.setOnClickListener(v -> updateAssignment());
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, dayOfMonth);
            selected.set(Calendar.HOUR_OF_DAY, 23);
            selected.set(Calendar.MINUTE, 59);

            edtDueDate.setText(displayFormat.format(selected.getTime()));
            edtDueDate.setTag(serverFormat.format(selected.getTime()));
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateAssignment() {
        String title = edtTitle.getText().toString().trim();
        String desc = edtDescription.getText().toString().trim();
        String date = (edtDueDate.getTag() != null) ? edtDueDate.getTag().toString() : currentAssignment.getDueDate();

        if (title.isEmpty()) {
            Toast.makeText(this, "Tiêu đề không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        currentAssignment.setTitle(title);
        currentAssignment.setDescription(desc);
        currentAssignment.setDueDate(date);

        apiService.saveAssignment(currentAssignment).enqueue(new Callback<AssignmentDTO>() {
            @Override
            public void onResponse(Call<AssignmentDTO> call, Response<AssignmentDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LecturerAssignmentDetailActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Thông báo cho màn hình danh sách cần load lại
                    finish();
                } else {
                    Toast.makeText(LecturerAssignmentDetailActivity.this, "Lỗi cập nhật: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<AssignmentDTO> call, Throwable t) {
                Toast.makeText(LecturerAssignmentDetailActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa bài tập này không?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteAssignment())
                .setNegativeButton("Hủy", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteAssignment() {
        Integer id = currentAssignment.getAssignmentId();

        if (id == null) {
            Toast.makeText(this, "Assignment ID = null, không thể xóa!", Toast.LENGTH_LONG).show();
            return;
        }

        apiService.deleteAssignment(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LecturerAssignmentDetailActivity.this, "Đã xóa bài tập!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(LecturerAssignmentDetailActivity.this,
                            "Không thể xóa: HTTP " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LecturerAssignmentDetailActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}