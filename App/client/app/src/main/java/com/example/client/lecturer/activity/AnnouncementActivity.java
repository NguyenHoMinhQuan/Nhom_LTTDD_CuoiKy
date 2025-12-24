package com.example.client.lecturer.activity;

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

import com.example.client.R;
import com.example.client.api.ApiService;
import com.example.client.lecturer.model.Announcement;
import com.example.client.lecturer.model.ClassDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnnouncementActivity extends AppCompatActivity {

    private Spinner spinnerClasses;
    private EditText etTitle, etBody;
    private Button btnPost;

    private ApiService apiService;
    private List<ClassDTO> classList = new ArrayList<>();
    private Integer selectedClassId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_announcement_create);

        // 1. Ánh xạ View
        spinnerClasses = findViewById(R.id.spinner_classes);
        etTitle = findViewById(R.id.et_announcement_title);
        etBody = findViewById(R.id.et_announcement_body);
        btnPost = findViewById(R.id.btn_post_announcement);

        initRetrofit();

        // 2. Tải danh sách lớp của giảng viên (Giả định LecturerId = 2)
        fetchLecturerClasses(2);

        // 3. Xử lý nút Đăng
        btnPost.setOnClickListener(v -> handlePostAnnouncement());
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/") // Địa chỉ máy ảo kết nối localhost
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    private void fetchLecturerClasses(Integer lecturerId) {
        apiService.getClassesByLecturer(lecturerId).enqueue(new Callback<List<ClassDTO>>() {
            @Override
            public void onResponse(Call<List<ClassDTO>> call, Response<List<ClassDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    classList = response.body();
                    setupSpinner();
                }
            }

            @Override
            public void onFailure(Call<List<ClassDTO>> call, Throwable t) {
                Toast.makeText(AnnouncementActivity.this, "Không thể tải danh sách lớp", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpinner() {
        // ArrayAdapter sẽ sử dụng phương thức toString() của ClassDTO để hiển thị Mã Lớp
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
        annc.setAuthorId("2"); // ID Giảng viên
        annc.setIsGlobal(false);

        apiService.postAnnouncement(annc).enqueue(new Callback<Announcement>() {
            @Override
            public void onResponse(Call<Announcement> call, Response<Announcement> response) {
                // 1. In ra URL thực tế mà App đã gọi
                Log.d("API_URL", "Request URL: " + call.request().url());

                if (response.isSuccessful()) {
                    Toast.makeText(AnnouncementActivity.this, "Thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // 2. In ra mã lỗi (404, 405, 500...)
                    Log.e("API_ERROR", "Error Code: " + response.code());

                    // 3. In ra nội dung lỗi chi tiết từ Server
                    try {
                        if (response.errorBody() != null) {
                            Log.e("API_ERROR", "Error Body: " + response.errorBody().string());
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