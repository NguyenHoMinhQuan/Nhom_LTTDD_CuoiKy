package com.example.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import androidx.appcompat.app.AppCompatActivity;

public class QuanLyKhoaHocActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_course);

        ImageView btnMenu = findViewById(R.id.btnMenu);
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> showMenu(v));
        }
    }

    private void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.nav_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_user) {
                startActivity(new Intent(this, QuanLyUserActivity.class));
                return true;
            } else if (id == R.id.menu_lop_hoc) {
                startActivity(new Intent(this, QuanLyLopHocActivity.class));
                return true;
            }
            return false;
        });
        popup.show();
    }
}