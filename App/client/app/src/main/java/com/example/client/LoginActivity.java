package com.example.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.client.HocVien.HomeActivity;
import com.example.client.lecturer.LecturerDashboardActivity;

public class LoginActivity extends AppCompatActivity {
    EditText edt_username, edt_pass;
    TextView txtv_lostpass;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.public_login);
        edt_username = findViewById(R.id.edt_username);
        edt_pass = findViewById(R.id.edt_pass);
        txtv_lostpass = findViewById(R.id.tv_lostpass);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username=edt_username.getText().toString().trim();
                String txt_pass=edt_pass.getText().toString().trim();
                if(!txt_username.isEmpty()&&!txt_pass.isEmpty()) {
                    if (txt_username.equals("admin") && txt_pass.equals("0000")) {
                        Toast.makeText(LoginActivity.this, "Admin đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                        startActivity(i);
                    } else if (txt_username.equals("giangvien") && txt_pass.equals("1234")) {
                        Toast.makeText(LoginActivity.this, "Giảng viên đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, LecturerDashboardActivity.class);
                        startActivity(i);
                    } else if (txt_username.equals("hocvien") && txt_pass.equals("1111")) {
                        Toast.makeText(LoginActivity.this, "Học viên đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        txtv_lostpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,ForgotPassActivity.class);
                startActivity(i);
            }
        });


    }

}