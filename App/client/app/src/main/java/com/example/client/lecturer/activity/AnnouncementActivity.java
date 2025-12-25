package com.example.client.lecturer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.client.Login.LoginActivity;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
import com.example.client.lecturer.model.Announcement;
import com.example.client.lecturer.model.ClassDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnnouncementActivity extends AppCompatActivity {

    private Spinner spinnerClasses;
    private EditText etTitle, etBody;
    private Button btnPost;

    private ApiService apiService;
    private List<ClassDTO> classList = new ArrayList<>();
    private Integer selectedClassId = null;

    // Biến lưu ID giảng viên hiện tại
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_announcement_create);

        // 🟢 1. LẤY ID TỪ PREFS
        SharedPreferences prefs = getSharedPreferences("AUTH_PREFS", MODE_PRIVATE);
        currentUserId = prefs.getInt("USER_ID", -1);

        // Kiểm tra bảo mật: Nếu chưa đăng nhập thì đá về Login
        if (currentUserId == -1) {
            Toast.makeText(this, "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        // 2. Ánh xạ View
        spinnerClasses = findViewById(R.id.spinner_classes);
        etTitle = findViewById(R.id.et_announcement_title);
        etBody = findViewById(R.id.et_announcement_body);
        btnPost = findViewById(R.id.btn_post_announcement);

        initRetrofit();

        // 🟢 3. GỌI API VỚI ID THỰC TẾ
        fetchLecturerClasses(currentUserId);

        // 4. Xử lý nút Đăng
        btnPost.setOnClickListener(v -> handlePostAnnouncement());
    }

    private void initRetrofit() {
        apiService = ApiClient.getClient(this).create(ApiService.class);
    }

    private void fetchLecturerClasses(Integer lecturerId) {
        apiService.getClassesByLecturer(lecturerId).enqueue(new Callback<List<ClassDTO>>() {
            @Override
            public void onResponse(Call<List<ClassDTO>> call, Response<List<ClassDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    classList = response.body();

                    if (classList.isEmpty()) {
                        Toast.makeText(AnnouncementActivity.this, "Bạn chưa được phân công lớp nào", Toast.LENGTH_SHORT).show();
                    }

                    setupSpinner();
                }
            }

            @Override
            public void onFailure(Call<List<ClassDTO>> call, Throwable t) {
                Toast.makeText(AnnouncementActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpinner() {
        // ArrayAdapter sẽ sử dụng phương thức toString() của ClassDTO để hiển thị Mã Lớp
        // Đảm bảo class ClassDTO của bạn đã override hàm toString() trả về tên lớp/mã lớp nhé
        ArrayAdapter<ClassDTO> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, classList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClasses.setAdapter(adapter);

        spinnerClasses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedClassId = classList.get(position).getClassId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedClassId = null;
            }
        });
    }

    private void handlePostAnnouncement() {
        String title = etTitle.getText().toString().trim();
        String body = etBody.getText().toString().trim();

        if (title.isEmpty() || body.isEmpty() || selectedClassId == null) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ và chọn lớp học", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng Announcement gửi lên Server
        Announcement annc = new Announcement();
        annc.setTitle(title);
        annc.setBody(body);
        annc.setTargetClassId(String.valueOf(selectedClassId));

        // 🟢 4. DÙNG ID THỰC TẾ LÀM AUTHOR
        annc.setAuthorId(String.valueOf(currentUserId));

        annc.setIsGlobal(false);

        apiService.postAnnouncement(annc).enqueue(new Callback<Announcement>() {
            @Override
            public void onResponse(Call<Announcement> call, Response<Announcement> response) {
                // Log để debug
                Log.d("API_URL", "Request URL: " + call.request().url());

                if (response.isSuccessful()) {
                    Toast.makeText(AnnouncementActivity.this, "Đăng thông báo thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng màn hình tạo để quay về màn hình trước
                } else {
                    Log.e("API_ERROR", "Error Code: " + response.code());
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e("API_ERROR", "Error Body: " + errorBody);
                            Toast.makeText(AnnouncementActivity.this, "Lỗi server: " + errorBody, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Announcement> call, Throwable t) {
                Toast.makeText(AnnouncementActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}