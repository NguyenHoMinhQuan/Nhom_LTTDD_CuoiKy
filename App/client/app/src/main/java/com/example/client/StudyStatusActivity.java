package com.example.client;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
public class StudyStatusActivity extends BaseHocVienActivity {


    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private StudyViewPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_status);

        //ánh xạ

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // 2. Gọi hàm setup header
        setupCommonHeader();


        // cài adapter
        adapter = new StudyViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        //Kết nối tablayout với viewpager(tự động đổi tên tab)

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Lịch học");
                    break;
                case 1:
                    tab.setText("Điểm");
                    break;
                case 2:
                    tab.setText("Nhiệm vụ");
                    break;

            }
        }).attach();



    }












}
