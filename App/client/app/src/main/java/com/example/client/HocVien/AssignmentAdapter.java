package com.example.client.HocVien;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.HocVien.Models.HocVien_BaiTapDto;
import com.example.client.HocVien.Models.HocVien_XemDiemDto;
import com.example.client.R;

import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder> {

    private List<HocVien_XemDiemDto> mList;
    private OnAssignmentClickListener mListener;

    //  Interface để bắt sự kiện click ra bên ngoài Fragment
    public interface OnAssignmentClickListener {
        void onAssignmentClick(HocVien_XemDiemDto assignment); // <-- Sửa ở đây
    }

    // 2. Constructor
    public AssignmentAdapter(List<HocVien_XemDiemDto> list, OnAssignmentClickListener listener) {
        this.mList = list;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hocvien_item_baitap, parent, false);
        return new AssignmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentViewHolder holder, int position) {
        HocVien_XemDiemDto item = mList.get(position);
        if (item == null) return;

        // Điền dữ liệu
        holder.tvTitle.setText(item.getAssignmentTitle());

        // Xử lý hiển thị ngày (Nếu null thì ghi Không thời hạn)
        if(item.getDueDate() != null) {
            holder.tvDueDate.setText("Hạn: " + item.getDueDate().replace("T", " ")); // Bỏ chữ T trong format ngày giờ
        } else {
            holder.tvDueDate.setText("Không thời hạn");
        }

        // --- XỬ LÝ MÀU SẮC TRẠNG THÁI ---
        String status = item.getSubmitStatus();
        if (status != null && status.equalsIgnoreCase("Đã nộp")) {
            holder.tvStatus.setText("ĐÃ NỘP");
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Màu xanh lá
        } else {
            holder.tvStatus.setText("CHƯA NỘP");
            holder.tvStatus.setTextColor(Color.parseColor("#F44336")); // Màu đỏ
        }

        // Nếu có điểm thì hiện điểm thay vì trạng thái
        if (item.getGrade() != null) {
            holder.tvStatus.setText("Điểm: " + item.getGrade());
            holder.tvStatus.setTextColor(Color.BLUE);
        }

        // Bắt sự kiện Click vào cả cái card
        holder.itemView.setOnClickListener(v -> mListener.onAssignmentClick(item));
    }

    @Override
    public int getItemCount() {
        if (mList != null) return mList.size();
        return 0;
    }

    // 3. ViewHolder class
    public static class AssignmentViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDueDate, tvStatus;

        public AssignmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvAssignmentTitle);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}