package com.example.client.HocVien; // Sửa lại package của bạn

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.client.LoginActivity;
import com.example.client.R;
import com.example.client.StudyStatusActivity;

// Class này chứa logic chung cho toàn bộ App
public class BaseHocVienActivity extends AppCompatActivity {

    // 1. Hàm thiết lập Header chung (Gọi hàm này ở các trang con)

    protected void setupCommonHeader() {
        // Xử lý Logo -> Về Home
        ImageView imgLogo = findViewById(R.id.imgLogo);
        if (imgLogo != null) {
            imgLogo.setOnClickListener(v -> {
                // Nếu đang ở Home rồi thì không làm gì hoặc load lại
                if (this instanceof HomeActivity) return;

                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }

        // Xử lý Avatar -> Hiện Menu
        ImageView imgAvatar = findViewById(R.id.imgAvatar); // Đảm bảo ID avatar giống nhau ở mọi layout
        if (imgAvatar != null) {
            imgAvatar.setOnClickListener(v -> {
                showUserMenuDialog();
            });
        }


    }

    // 2. Hàm Menu Popup (Đưa từ HomeActivity sang đây để ai cũng dùng được)
    protected void showUserMenuDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_menu_hv);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setGravity(Gravity.CENTER);
        }

        // Ánh xạ và xử lý click menu...
        Button btnMyCourse = dialog.findViewById(R.id.menu_btn_my_course);
        Button btnStatus = dialog.findViewById(R.id.menu_btn_status);
        Button btnInfo = dialog.findViewById(R.id.menu_btn_info);
        Button btnLogout = dialog.findViewById(R.id.menu_btn_logout);

        btnMyCourse.setOnClickListener(v -> {
            dialog.dismiss();
            navigate(MyCoursesActivity.class);
        });

        btnInfo.setOnClickListener(v -> {
            dialog.dismiss();
            navigate(ProfileActivity.class);
        });

        btnStatus.setOnClickListener(v -> {
            dialog.dismiss();
            navigate(StudyStatusActivity.class); // Trang chứa 3 Tab
        });

        btnLogout.setOnClickListener(v -> {
            dialog.dismiss();
            navigate(LoginActivity.class);// trang đăng nhập
        });

        dialog.show();
    }

    // 3. Hàm chuyển trang chung (Giữ nguyên)
    protected void navigate(Class targetActivity) {
        // Nếu đang ở trang đó rồi thì không chuyển nữa (tránh lỗi)
        if (this.getClass() == targetActivity) return;

        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}