package com.example.client.lecturer.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
import com.example.client.lecturer.adapter.SubmissionAdapter;
import com.example.client.lecturer.model.AssignmentDTO;
import com.example.client.lecturer.model.SubmissionDTO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity hiển thị chi tiết bài tập, cho phép chỉnh sửa/xóa và xem danh sách sinh viên đã nộp.
 */
public class LecturerAssignmentDetailActivity extends AppCompatActivity {

    private EditText edtTitle, edtDescription, edtDueDate;
    private Button btnUpdate, btnDelete;
    private TextView tvSubmissionCount;
    private ApiService apiService;
    private AssignmentDTO currentAssignment;
    private int lecturerId;
    private SubmissionAdapter submissionAdapter;
    private List<SubmissionDTO> submissionList; // dữ liệu hiện tại


    private SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_assignment_detail);

        // Lấy lecturerId
        SharedPreferences prefs = getSharedPreferences("AUTH_PREFS", MODE_PRIVATE);
        lecturerId = prefs.getInt("USER_ID", -1);

        // Nhận dữ liệu từ Intent
        currentAssignment = (AssignmentDTO) getIntent().getSerializableExtra("ASSIGNMENT_DATA");

        if (currentAssignment == null) {
            Toast.makeText(this, "Không tìm thấy dữ liệu bài tập!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Ánh xạ View
        edtTitle = findViewById(R.id.edt_title);
        edtDescription = findViewById(R.id.edt_description);
        edtDueDate = findViewById(R.id.edt_due_date);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);
        tvSubmissionCount = findViewById(R.id.tv_submission_count);

        apiService = ApiClient.getClient(this).create(ApiService.class);

        // Đổ dữ liệu vào View
        edtTitle.setText(currentAssignment.getTitle());
        edtDescription.setText(currentAssignment.getDescription());

        String rawDate = currentAssignment.getDueDate();
        if (rawDate != null) {
            String dateOnly = rawDate.contains("T") ? rawDate.split("T")[0] : rawDate;
            edtDueDate.setText(dateOnly);
            edtDueDate.setTag(rawDate);
        }

        loadSubmissionCount();

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        edtDueDate.setOnClickListener(v -> showDatePicker());
        btnUpdate.setOnClickListener(v -> updateAssignment());
        btnDelete.setOnClickListener(v -> confirmDelete());

        tvSubmissionCount.setOnClickListener(v -> {
            apiService.getSubmissions(currentAssignment.getAssignmentId())
                    .enqueue(new Callback<List<SubmissionDTO>>() {
                        @Override
                        public void onResponse(Call<List<SubmissionDTO>> call, Response<List<SubmissionDTO>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                showSubmissionListDialog(response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<SubmissionDTO>> call, Throwable t) {
                            Toast.makeText(LecturerAssignmentDetailActivity.this, "Không thể tải danh sách nộp bài", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        ;
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

    // Cập nhật bài tập
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
                    setResult(RESULT_OK);
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

    // Xác nhận xóa
    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa bài tập này không?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteAssignment())
                .setNegativeButton("Hủy", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    // Xóa bài tập
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
                            "Không thể xóa: HTTP " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LecturerAssignmentDetailActivity.this,
                        "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Load số sinh viên đã nộp
    private void loadSubmissionCount() {
        Integer assignmentId = currentAssignment.getAssignmentId();
        if (assignmentId == null) return;

        apiService.getSubmissionCount(assignmentId).enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful() && response.body() != null) {
                    long count = response.body();
                    tvSubmissionCount.setText("Số sinh viên đã nộp: " + count);
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Toast.makeText(LecturerAssignmentDetailActivity.this,
                        "Không thể tải số lượng nộp bài", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showSubmissionListDialog(List<SubmissionDTO> submissions) {
        submissionList = submissions;

        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        submissionAdapter = new SubmissionAdapter(this, submissionList, this::showSubmissionDetailDialog);
        recyclerView.setAdapter(submissionAdapter);

        new AlertDialog.Builder(this)
                .setTitle("Danh sách sinh viên đã nộp")
                .setView(recyclerView)
                .setPositiveButton("Đóng", null)
                .show();
    }


    // Dialog chi tiết để xem file + ngày nộp + nhập/chấm điểm
    private void showSubmissionDetailDialog(SubmissionDTO submission) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.lecturer_dialog_submission_detail, null);

        TextView tvName = view.findViewById(R.id.tvStudentName);
        TextView tvSubmittedAt = view.findViewById(R.id.tvSubmittedAt);
        TextView tvFileUrl = view.findViewById(R.id.tvFileUrl);
        EditText edtGrade = view.findViewById(R.id.edtGrade);
        Button btnSave = view.findViewById(R.id.btnSaveGrade);

        tvName.setText(submission.getStudentName());
        tvSubmittedAt.setText("Ngày nộp: " + submission.getSubmittedAt());
        tvFileUrl.setText("File: " + submission.getFileUrl());
        edtGrade.setText(submission.getGrade() != null ? submission.getGrade().toString() : "");

        AlertDialog dialog = builder.setView(view).create();

        btnSave.setOnClickListener(v -> {
            String gradeStr = edtGrade.getText().toString().trim();
            if (!gradeStr.isEmpty()) {
                double grade = Double.parseDouble(gradeStr);
                saveGrade(submission.getSubmissionId(), grade, dialog);
            }
        });

        dialog.show();
    }

    private void saveGrade(Integer submissionId, double grade, AlertDialog dialog) {
        if (grade < 0 || grade > 10) {
            Toast.makeText(this, "Điểm phải từ 0 đến 10", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.gradeSubmission(submissionId, grade).enqueue(new Callback<SubmissionDTO>() {
            @Override
            public void onResponse(Call<SubmissionDTO> call, Response<SubmissionDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SubmissionDTO updated = response.body();

                    for (int i = 0; i < submissionList.size(); i++) {
                        SubmissionDTO s = submissionList.get(i);
                        if (s.getSubmissionId().equals(updated.getSubmissionId())) {
                            s.setGrade(updated.getGrade()); // chỉ cập nhật điểm
                            break;
                        }
                    }
                    submissionAdapter.notifyDataSetChanged();

                    Toast.makeText(LecturerAssignmentDetailActivity.this, "Lưu điểm thành công!", Toast.LENGTH_SHORT).show();

                    // ✅ Đóng dialog chi tiết ngay
                    dialog.dismiss();

                    loadSubmissionCount(); // cập nhật số lượng nộp nếu cần
                }
            }

            @Override
            public void onFailure(Call<SubmissionDTO> call, Throwable t) {
                Toast.makeText(LecturerAssignmentDetailActivity.this, "Lỗi khi lưu điểm!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
