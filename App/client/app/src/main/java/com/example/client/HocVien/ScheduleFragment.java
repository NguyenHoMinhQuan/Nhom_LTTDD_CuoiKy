package com.example.client.HocVien;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.client.R;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.Nullable;


public class ScheduleFragment extends Fragment {
    private TextInputEditText edtTimKiemUsername ;
    private Button btnXemLich;
    private LinearLayout layoutLichHocContainer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Liên kết với layout cũ của bạn: hocvien_layout_calender_school
        return inflater.inflate(R.layout.hocvien_layout_calender_school, container, false);
    }
}