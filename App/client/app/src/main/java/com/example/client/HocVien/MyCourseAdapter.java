package com.example.client.HocVien;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.HocVien.Models.HocVien_NhomLopDto;
import com.example.client.R;

import java.util.List;

public class MyCourseAdapter extends RecyclerView.Adapter<MyCourseAdapter.CourseViewHolder> {

    private List<HocVien_NhomLopDto> courseList;

    public MyCourseAdapter(List<HocVien_NhomLopDto> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hocvien_item_mycourse, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        HocVien_NhomLopDto course = courseList.get(position);

        // Gán dữ liệu
        holder.tvCourseName.setText(course.getCourseName());
        holder.tvCourseCode.setText("Mã môn: " + course.getCourseCode());

        if (course.getLecturerName() != null) {
            holder.tvLecturer.setText("GV: " + course.getLecturerName());
        } else {
            holder.tvLecturer.setText("GV: Chưa phân công");
        }

        holder.tvClassCode.setText("Lớp: " + course.getClassCode() + " (" + course.getSemester() + ")");

        // Set ảnh (Nếu bạn muốn mỗi môn 1 ảnh khác nhau thì xử lý ở đây, tạm thời để mặc định trong XML)
        // holder.imgCourse.setImageResource(...);
    }

    @Override
    public int getItemCount() {
        return courseList != null ? courseList.size() : 0;
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvCourseCode, tvLecturer, tvClassCode;
        ImageView imgCourse;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvCourseCode = itemView.findViewById(R.id.tvCourseCode);
            tvLecturer = itemView.findViewById(R.id.tvLecturer);
            tvClassCode = itemView.findViewById(R.id.tvClassCode);
            imgCourse = itemView.findViewById(R.id.imgCourse);
        }
    }
}