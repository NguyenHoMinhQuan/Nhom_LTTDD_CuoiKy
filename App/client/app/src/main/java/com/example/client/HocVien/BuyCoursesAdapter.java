package com.example.client.HocVien;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;

import java.util.List;
public class BuyCoursesAdapter extends RecyclerView.Adapter<BuyCoursesAdapter.ViewHolder> {

    private List<BuyCourses> mList;

    public BuyCoursesAdapter(List<BuyCourses> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Ánh xạ layout item bạn đã gửi: hocvien_item_buy_course.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hocvien_item_thongbao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BuyCourses course = mList.get(position);
        if (course == null) return;

        holder.tvName.setText(course.getName());
        holder.tvPrice.setText(course.getPrice());
        holder.tvDuration.setText(course.getDuration());

        // Nếu chưa có ảnh mon3, hãy đổi thành R.drawable.ic_launcher_background để test
        holder.imgCourse.setImageResource(course.getImageResId());

        // Xử lý sự kiện nút MUA NGAY
        holder.btnBuy.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Đã chọn mua: " + course.getName(), Toast.LENGTH_SHORT).show();
            // Sau này bạn có thể code chuyển sang màn hình thanh toán tại đây
        });
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCourse;
        TextView tvName, tvPrice, tvDuration;
        Button btnBuy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ ID từ file xml hocvien_item_buy_course
            imgCourse = itemView.findViewById(R.id.ivItemImage);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvPrice = itemView.findViewById(R.id.tvItemPrice);
            tvDuration = itemView.findViewById(R.id.tvItemSub);
            btnBuy = itemView.findViewById(R.id.btnBuy);
        }
    }
}
