package com.example.client.Admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.client.R;
import com.example.client.api.AdminResponse;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<AdminResponse.CourseRow> mListCourse;
    private IClickItemCourse iClickItemCourse;

    // Interface để xử lý sự kiện click vào một dòng trong bảng
    public interface IClickItemCourse {
        void onClickItemCourse(AdminResponse.CourseRow course);
    }

    public CourseAdapter(List<AdminResponse.CourseRow> list, IClickItemCourse listener) {
        this.mListCourse = list;
        this.iClickItemCourse = listener;
    }

    // Cập nhật dữ liệu và làm mới bảng
    public void setData(List<AdminResponse.CourseRow> list) {
        this.mListCourse = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng layout item_row_course.xml đã thiết kế cho khóa học
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        AdminResponse.CourseRow course = mListCourse.get(position);
        if (course == null) return;

        holder.tvId.setText("#" + course.id);
        holder.tvMaMon.setText(course.maMon);
        holder.tvTenMon.setText(course.tenMon);
        holder.tvTinChi.setText(String.valueOf(course.tinChi));
        holder.tvMoTa.setText(course.moTa);

        // Truyền đúng đối tượng course vào listener
        holder.itemView.setOnClickListener(v -> iClickItemCourse.onClickItemCourse(course));
    }

    @Override
    public int getItemCount() {
        return mListCourse != null ? mListCourse.size() : 0;
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        // 1. Phải khai báo biến ở đây
        TextView tvId, tvMaMon, tvTenMon, tvTinChi, tvMoTa;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            // 2. Ánh xạ (Sau bước này chữ sẽ hết màu đỏ)
            tvId = itemView.findViewById(R.id.tvId);
            tvMaMon = itemView.findViewById(R.id.tvMaMon);
            tvTenMon = itemView.findViewById(R.id.tvTenMon);
            tvTinChi = itemView.findViewById(R.id.tvTinChi);
            tvMoTa = itemView.findViewById(R.id.tvMoTa);
        }
    }
}