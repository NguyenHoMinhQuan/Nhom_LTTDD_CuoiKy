package com.example.client.HocVien;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.client.HocVien.Models.SoYeuLyLichModel;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    // khai báo ô nhập dữ liệu Edit text
    TextInputEditText edtStudentNumber, edtFullName, edtEmail, edtFaculty, edtYear, edtDateOfBirth;
    LinearLayout btnBack ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hocvien_layout_infor_student);

        // ánh xạ
        btnBack = findViewById(R.id.btnBack);
        edtStudentNumber = findViewById(R.id.edtStudentNumber);
        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtFaculty = findViewById(R.id.edtFaculty);
        edtYear = findViewById(R.id.edtYear);
        edtDateOfBirth = findViewById(R.id.edtDateOfBirth);

        // Xử lý sự kiện
        if(btnBack != null){
            btnBack.setOnClickListener(v -> {
                finish();
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
            });
        }

        SharedPreferences prefs = getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE);
        String username = prefs.getString("USERNAME", "");
        LayDuLieu(username);
    }
    private void LayDuLieu(String username){
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<SoYeuLyLichModel> call = apiService.LaySoYeuLyLich(username);
        call.enqueue(new Callback<SoYeuLyLichModel>() {
            @Override
            public void onResponse(Call<SoYeuLyLichModel> call, Response<SoYeuLyLichModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SoYeuLyLichModel data = response.body();

                    // 3. GẮN DỮ LIỆU TỪ JSON VÀO Ô NHẬP LIỆU

                    // MSSV
                    edtStudentNumber.setText(data.getStudentNumber());

                    // Họ tên
                    edtFullName.setText(data.getFullName());

                    // Email
                    edtEmail.setText(data.getEmail());

                    // Khoa
                    edtFaculty.setText(data.getFaculty());

                    // Niên khóa
                    edtYear.setText(data.getYear());

                    // Ngày sinh (Check null vì json có thể null)
                    if (data.getDateOfBirth() != null) {
                        edtDateOfBirth.setText(data.getDateOfBirth());
                    } else {
                        edtDateOfBirth.setText(""); // Để trống nếu null
                    }

                } else {
                    Toast.makeText(ProfileActivity.this, "Không lấy được dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SoYeuLyLichModel> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
