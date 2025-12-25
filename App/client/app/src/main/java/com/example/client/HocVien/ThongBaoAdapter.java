package com.example.client.HocVien;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.HocVien.Models.ThongBaoModel;
import com.example.client.R;

import java.util.List;
import java.util.Random;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ViewHolder> {

    private List<ThongBaoModel> mList;

    public ThongBaoAdapter(List<ThongBaoModel> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hocvien_item_thongbao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThongBaoModel item = mList.get(position);
        if (item == null) return;

        // 1. Gán Tiêu đề
        holder.tvTitle.setText(item.getTieuDe());

        // 2. Gán Nội dung
        holder.tvBody.setText(item.getNoiDung());

        // 3. Gán Ngày tạo
        // Cắt chuỗi ngày cho gọn (VD: 2023-12-20T10:00 -> 2023-12-20)
        String rawDate = item.getNgayTao();
        if (rawDate != null && rawDate.length() >= 10) {
            holder.tvDate.setText(rawDate.substring(0, 10));
        } else {
            holder.tvDate.setText(rawDate);
        }

        // 4. Random ảnh cho sinh động
        holder.imgIcon.setImageResource(R.drawable.ute);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView tvTitle, tvBody, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ đúng ID trong file XML hocvien_item_thongbao.xml
            imgIcon = itemView.findViewById(R.id.ivAnnouncementIcon);
            tvTitle = itemView.findViewById(R.id.tvAnnouncementTitle);
            tvBody = itemView.findViewById(R.id.tvAnnouncementBody);
            tvDate = itemView.findViewById(R.id.tvAnnouncementDate);
        }
    }
}