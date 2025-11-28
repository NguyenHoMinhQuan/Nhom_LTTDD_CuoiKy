package com.example.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText edt_username, edt_pass;
    TextView txtv_lostpass;
    Button btn_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_login);
        edt_username = findViewById(R.id.edt_username);
        edt_pass = findViewById(R.id.edt_pass);
        txtv_lostpass = findViewById(R.id.tv_lostpass);
        btn_login = findViewById(R.id.btn_login);
//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String txt_username=edt_username.getText().toString().trim();
//                String txt_pass=edt_pass.getText().toString().trim();
//                if(!txt_username.isEmpty()&&!txt_pass.isEmpty()){
//                    Intent i = new Intent(LoginActivity)
//                    }
//            }
//        });
        txtv_lostpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,ForgotPassActivity.class);
                startActivity(i);
            }
        });

    }
}
