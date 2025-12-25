package com.example.client.HocVien;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.client.HocVien.Models.TinNhanModel;
import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.ApiService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    int idLop;
    String tenLop;
    RecyclerView rv;
    EditText edtInput;
    Button btnGui;
    ImageView btnBack, btnFeedback;
    TextView tvTitle;
    List<TinNhanModel> listChat = new ArrayList<>();
    TinNhanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hocvien_activity_chat);

        idLop = getIntent().getIntExtra("ID_LOP", 0);
        tenLop = getIntent().getStringExtra("TEN_LOP");

        rv = findViewById(R.id.rvTinNhan);
        edtInput = findViewById(R.id.edtChatInput);
        btnGui = findViewById(R.id.btnChatSend);
        btnBack = findViewById(R.id.btnBackChat);
        btnFeedback = findViewById(R.id.btnGoToFeedback);
        tvTitle = findViewById(R.id.tvTitleLop);

        if(tenLop != null) tvTitle.setText(tenLop);

        adapter = new TinNhanAdapter(listChat);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        loadData();

        btnBack.setOnClickListener(v -> finish());

        // Chuyển sang màn hình Feedback
        btnFeedback.setOnClickListener(v -> {
            Intent intent = new Intent(ChatActivity.this, FeedbackActivity.class);
            intent.putExtra("ID_LOP", idLop);
            intent.putExtra("TEN_LOP", tenLop);
            startActivity(intent);
        });

        btnGui.setOnClickListener(v -> {
            String txt = edtInput.getText().toString().trim();
            if(!txt.isEmpty()){
                // Tạm thời ID người gửi là 1
                TinNhanModel msg = new TinNhanModel(idLop, 1, txt);
                ApiClient.getClient(this).create(ApiService.class).guiTinNhan(msg).enqueue(new Callback<TinNhanModel>() {
                    @Override public void onResponse(Call<TinNhanModel> call, Response<TinNhanModel> response) {
                        if(response.isSuccessful()) {
                            edtInput.setText("");
                            loadData();
                        }
                    }
                    @Override public void onFailure(Call<TinNhanModel> call, Throwable t) {}
                });
            }
        });
    }

    void loadData(){
        ApiClient.getClient(this).create(ApiService.class).layTinNhan(idLop).enqueue(new Callback<List<TinNhanModel>>() {
            @Override public void onResponse(Call<List<TinNhanModel>> call, Response<List<TinNhanModel>> response) {
                if(response.isSuccessful() && response.body() != null){
                    listChat.clear();
                    listChat.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    rv.scrollToPosition(listChat.size()-1);
                }
            }
            @Override public void onFailure(Call<List<TinNhanModel>> call, Throwable t) {}
        });
    }
}