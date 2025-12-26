package com.example.client.HocVien;

import android.content.Context; // Nhớ import cái này
import android.content.Intent;
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

    // === SỬA Ở ĐÂY ===
    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        HocVien_NhomLopDto course = courseList.get(position);

        // 1. Gán dữ liệu hiển thị (Code cũ của bạn)
        holder.tvCourseName.setText(course.getCourseName());
        holder.tvCourseCode.setText("Mã môn: " + course.getCourseCode());

        if (course.getLecturerName() != null) {
            holder.tvLecturer.setText("GV: " + course.getLecturerName());
        } else {
            holder.tvLecturer.setText("GV: Chưa phân công");
        }

        holder.tvClassCode.setText("Lớp: " + course.getClassCode() + " (" + course.getSemester() + ")");

        // 2. Thêm sự kiện Click (MỚI THÊM)
        holder.itemView.setOnClickListener(v -> {
            // Lấy Context từ View
            Context context = v.getContext();

            android.util.Log.e("CHECK_LOI", "Đang bấm vào môn: " + course.getCourseName() + " | ID Lấy được là: " + course.getClassId());

            Intent intent = new Intent(context, ChatActivity.class);

            // Truyền dữ liệu sang màn hình Chat
            // Lưu ý: Đảm bảo DTO của bạn có hàm getClassId() trả về int
            intent.putExtra("ID_LOP", course.getClassId());
            intent.putExtra("TEN_LOP", course.getCourseName()); // Hoặc getClassName() tùy bạn muốn hiện tên gì

            context.startActivity(intent);
        });
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