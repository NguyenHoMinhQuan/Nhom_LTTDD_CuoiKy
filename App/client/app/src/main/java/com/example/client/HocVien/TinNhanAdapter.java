package com.example.client.HocVien;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.HocVien.Models.TinNhanModel;
import com.example.client.R;

import java.util.List;

public class TinNhanAdapter extends RecyclerView.Adapter<TinNhanAdapter.ViewHolder> {

    private List<TinNhanModel> list;
    private int currentUserId;

    public TinNhanAdapter(List<TinNhanModel> list,int currentUserId) {
        this.list = list;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hocvien_item_tin_nhan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TinNhanModel item = list.get(position);
        holder.tvNoiDung.setText(item.getContent());

        // 2.  so sánh: Dùng ID
        // item.getSenderId() lấy từ JSON, currentUserId lấy từ máy
        boolean isMe = (item.getSenderId() != null && item.getSenderId() == currentUserId);

        if (isMe) {
            // ... (Code giao diện bên trong giữ nguyên)
            holder.layoutMsg.setGravity(Gravity.END);
            holder.tvNguoi.setText("Tôi");
            holder.tvNguoi.setTextColor(Color.BLUE);
        } else {
            // ...
            holder.layoutMsg.setGravity(Gravity.START);
            holder.tvNguoi.setText(item.getSenderName());
            holder.tvNguoi.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNguoi, tvNoiDung;
        LinearLayout layoutMsg; // Layout bao ngoài để chỉnh Gravity

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNguoi = itemView.findViewById(R.id.tvNguoiGui);
            tvNoiDung = itemView.findViewById(R.id.tvNoiDung);
            // Bạn cần bọc tvNguoi và tvNoiDung trong 1 LinearLayout ở file xml để căn chỉnh
            layoutMsg = (LinearLayout) itemView.findViewById(R.id.linearLayoutMsg);
        }
    }
}