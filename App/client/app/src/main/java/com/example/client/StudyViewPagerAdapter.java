package com.example.client;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class StudyViewPagerAdapter extends FragmentStateAdapter {
    public StudyViewPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        //tab 0 : calender , tab 1 : score , tab 2 : assignment
        switch (position) {
            case 0:
                return new ScheduleFragment();
            case 1:
                return new ScoreFragment();
            case 2:
                return new AssignmentFragment();
            default:
                return new ScheduleFragment();
        }

    }
    public int getItemCount() {
        return 3;// tổng 3 tab
    }
}
