package com.example.client.HocVien;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.client.HocVien.Models.DanhGiaModel;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
import com.google.android.material.textfield.TextInputEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {
    int idLop;
    String tenLop;

    // Khai báo view theo ID trong XML
    ImageView btnBack;
    TextView tvCourseName;
    RatingBar ratingBar;
    TextInputEditText etComment;
    Button btnCancel, btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hocvien_feedback); // File xml bạn gửi

        idLop = getIntent().getIntExtra("ID_LOP", 0);
        tenLop = getIntent().getStringExtra("TEN_LOP");

        // Ánh xạ
        btnBack = findViewById(R.id.btnBack);
        tvCourseName = findViewById(R.id.tvCourseName);
        ratingBar = findViewById(R.id.ratingBar);
        etComment = findViewById(R.id.etComment); // ID trong TextInputEditText
        btnCancel = findViewById(R.id.btnCancel);
        btnSend = findViewById(R.id.btnSend);

        if(tenLop != null) tvCourseName.setText(tenLop);

        btnBack.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());

        btnSend.setOnClickListener(v -> {
            int soSao = (int) ratingBar.getRating();
            String noiDung = etComment.getText().toString();

            if(soSao == 0) {
                Toast.makeText(this, "Vui lòng chọn số sao", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạm thời ID sinh viên là 1
            DanhGiaModel model = new DanhGiaModel(idLop, 1, soSao, noiDung);

            ApiClient.getClient(this).create(ApiService.class).guiDanhGia(model).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(FeedbackActivity.this, "Cảm ơn đánh giá của bạn!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(FeedbackActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}