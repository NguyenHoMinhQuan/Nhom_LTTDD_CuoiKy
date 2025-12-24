package com.example.client.HocVien;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.client.R;

import org.jetbrains.annotations.Nullable;


public class ScoreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Liên kết với layout cũ: hocvien_layout_score
        return inflater.inflate(R.layout.hocvien_layout_calender_school, container, false);
    }
}