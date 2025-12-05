package com.example.client.HocVien;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;

import java.util.List;

public class MyCourseAdapter extends RecyclerView.Adapter<MyCourseAdapter.MyCourseViewHolder> {

    private List<MyCourses> courseList;

    public MyCourseAdapter(List<MyCourses> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public MyCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Ánh xạ layout item_my_course_hv của bạn
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hocvien_item_mycourse, parent, false);
        return new MyCourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCourseViewHolder holder, int position) {
        MyCourses course = courseList.get(position);

        // Gán dữ liệu vào View
        holder.tvName.setText(course.getName());
        holder.tvDetail.setText(course.getDescription());

        // Gán ảnh (Nếu bạn chưa có ảnh 'mon3', hãy đổi thành R.drawable.ic_launcher_background để test)
        holder.imgCourse.setImageResource(course.getImageResId());
    }

    @Override
    public int getItemCount() {
        return courseList.size(); // Trả về số lượng phần tử
    }

    // ViewHolder để ánh xạ các view trong item layout
    public static class MyCourseViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCourse;
        TextView tvName, tvDetail;

        public MyCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ đúng ID trong file item_my_course_hv.xml
            imgCourse = itemView.findViewById(R.id.courseImageView);
            tvName = itemView.findViewById(R.id.courseNameTextView);
            tvDetail = itemView.findViewById(R.id.courseDetailTextView);
        }
    }
}
