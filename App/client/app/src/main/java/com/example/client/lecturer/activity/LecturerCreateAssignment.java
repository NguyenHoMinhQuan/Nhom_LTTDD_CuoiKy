package com.example.client.lecturer.activity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
import com.example.client.lecturer.model.AssignmentDTO;
import com.example.client.lecturer.model.ClassDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LecturerCreateAssignment extends AppCompatActivity {

    private Spinner spnSubject, spnClass;
    private EditText edtDueDate, edtDescription;
    private TextView tvWelcomeName;
    private Button btnSubmit, btnCancel;

    private ApiService apiService;
    private List<ClassDTO> lecturerClasses = new ArrayList<>();
    private int lecturerId;

    // Định dạng ngày hiển thị và gửi lên Server
    private SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_assignment);

        // 1. Lấy thông tin Giảng viên từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("AUTH_PREFS", MODE_PRIVATE);
        lecturerId = prefs.getInt("USER_ID", -1);
        String lecturerName = prefs.getString("USERNAME", "Giảng viên");

        if (lecturerId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 2. Ánh xạ View
        tvWelcomeName = findViewById(R.id.tv_welcome_name);
        spnSubject = findViewById(R.id.spn_subject);
        spnClass = findViewById(R.id.spn_class);
        edtDueDate = findViewById(R.id.edt_due_date);
        edtDescription = findViewById(R.id.edt_description);
        btnSubmit = findViewById(R.id.btn_submit);
        btnCancel = findViewById(R.id.btn_cancel);

        tvWelcomeName.setText("Hi, " + lecturerName);

        // 3. Khởi tạo API Service
        apiService = ApiClient.getClient(this).create(ApiService.class);

        // 4. Load dữ liệu khởi tạo
        setupSubjectSpinner();
        loadLecturerClasses();

        // 5. Thiết lập sự kiện
        setupDatePicker();
        btnCancel.setOnClickListener(v -> finish());
        btnSubmit.setOnClickListener(v -> performSubmit());
    }

    private void setupSubjectSpinner() {
        // Danh sách học phần (Có thể lấy từ API nếu cần, ở đây fix cứng demo)
        String[] subjects = {"Lập trình di động", "Cấu trúc dữ liệu", "Cơ sở dữ liệu", "Mạng máy tính"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSubject.setAdapter(adapter);
    }

    private void loadLecturerClasses() {
        apiService.getClassesByLecturer(lecturerId).enqueue(new Callback<List<ClassDTO>>() {
            @Override
            public void onResponse(Call<List<ClassDTO>> call, Response<List<ClassDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    lecturerClasses = response.body();
                    List<String> classNames = new ArrayList<>();
                    for (ClassDTO c : lecturerClasses) {
                        classNames.add(c.getClassCode() );
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(LecturerCreateAssignment.this,
                            android.R.layout.simple_spinner_item, classNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnClass.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<ClassDTO>> call, Throwable t) {
                Toast.makeText(LecturerCreateAssignment.this, "Không thể tải danh sách lớp!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupDatePicker() {
        edtDueDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                Calendar selected = Calendar.getInstance();
                selected.set(year, month, dayOfMonth);
                selected.set(Calendar.HOUR_OF_DAY, 23);
                selected.set(Calendar.MINUTE, 59);

                edtDueDate.setText(displayFormat.format(selected.getTime()));
                edtDueDate.setTag(serverFormat.format(selected.getTime())); // Lưu chuỗi ISO
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void performSubmit() {
        String subject = spnSubject.getSelectedItem().toString();
        String description = edtDescription.getText().toString().trim();
        String dueDateIso = (edtDueDate.getTag() != null) ? edtDueDate.getTag().toString() : "";
        int classIndex = spnClass.getSelectedItemPosition();

        if (description.isEmpty() || dueDateIso.isEmpty() || classIndex < 0) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy ClassId thật từ danh sách đã load
        Integer classId = lecturerClasses.get(classIndex).getClassId();

        // Đóng gói DTO
        AssignmentDTO dto = new AssignmentDTO();
        dto.setClassId(classId);
        dto.setTitle("Bài tập: " + subject);
        dto.setDescription(description);
        dto.setDueDate(dueDateIso);

        apiService.saveAssignment(dto).enqueue(new Callback<AssignmentDTO>() {
            @Override
            public void onResponse(Call<AssignmentDTO> call, Response<AssignmentDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LecturerCreateAssignment.this, "Giao bài tập thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại trang danh sách bài tập
                } else {
                    Toast.makeText(LecturerCreateAssignment.this, "Lỗi server: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AssignmentDTO> call, Throwable t) {
                Toast.makeText(LecturerCreateAssignment.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}