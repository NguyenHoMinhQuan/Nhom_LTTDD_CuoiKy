package com.example.client;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class DashboardCourseActivity extends AppCompatActivity {
    private ViewPager2 viewPagerBanner;
    private Handler sliderHandler = new Handler();
    private RecyclerView rvCourses;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_customer);

        // ánh xạ
        viewPagerBanner = findViewById(R.id.viewPagerBanner);
        rvCourses = findViewById(R.id.rvCourses);

        // xử lí nút back (quay lại main)
        View btnBack = findViewById(R.id.btnBack);
        if(btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        setupBanner();
        setupCourses();

    }

    // chuyện động cho banner
    private void setupBanner() {
        List<Integer> bannerColors = new ArrayList<>();
        bannerColors.add(Color.parseColor("#FFEB3B"));
        bannerColors.add(Color.parseColor("#4CAF50"));
        bannerColors.add(Color.parseColor("#2196F3"));

        BannerAdapter bannerAdapter = new BannerAdapter(bannerColors);
        viewPagerBanner.setAdapter(bannerAdapter);

        viewPagerBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);//trượt sau 3 giây
            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        public void run(){
            int currentItem = viewPagerBanner.getCurrentItem();
            int totalItems = viewPagerBanner.getAdapter().getItemCount()  ;
            if(currentItem < totalItems - 1) {
                viewPagerBanner.setCurrentItem(currentItem + 1);
            } else {
                viewPagerBanner.setCurrentItem(0);
            }
        }
    };

    // danh sách ảnh so le -dashboard
    private void setupCourses() {
        // StaggeredGridLayoutManager: tạo 2 cột so le
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvCourses.setLayoutManager(layoutManager);

        // Tạo dữ liệu giả
        List<String> courses = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            courses.add("Khóa học IT " + (i + 1));
        }

        CourseAdapter courseAdapter = new CourseAdapter(courses);
        rvCourses.setAdapter(courseAdapter);
    }

    class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
        List<Integer> colors;
        public BannerAdapter(List<Integer> colors) { this.colors = colors; }

        @NonNull @Override
        public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_item_banner, parent, false);
            return new BannerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
            // Tìm ImageView trong item_banner có id là ivBanner (nếu bạn đặt tên khác thì sửa ở đây)
            ImageView img = holder.itemView.findViewById(R.id.ivBanner);
            if (img != null) img.setBackgroundColor(colors.get(position));
        }

        @Override public int getItemCount() { return colors.size(); }
        class BannerViewHolder extends RecyclerView.ViewHolder {
            public BannerViewHolder(@NonNull View itemView) { super(itemView); }
        }
    }
    class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
        List<String> data;
        Random random = new Random();

        public CourseAdapter(List<String> data) { this.data = data; }

        @NonNull @Override
        public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CourseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_item_course, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
            holder.tvName.setText(data.get(position));

            // RANDOM CHIỀU CAO ẢNH
            int randomHeight = random.nextInt(200) + 300; // Cao từ 300 đến 500
            holder.ivImage.getLayoutParams().height = randomHeight;

            // Random màu sắc cho ảnh
            int color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
            holder.ivImage.setBackgroundColor(color);
        }
        @Override public int getItemCount() { return data.size(); }

        class CourseViewHolder extends RecyclerView.ViewHolder {
            ImageView ivImage; TextView tvName;
            public CourseViewHolder(@NonNull View itemView) {
                super(itemView);
                ivImage = itemView.findViewById(R.id.ivCourseImage);
                tvName = itemView.findViewById(R.id.tvCourseName);
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable); // Dừng slider khi thoát
    }
}
