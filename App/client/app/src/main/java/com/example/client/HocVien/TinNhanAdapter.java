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
<<<<<<< HEAD
    private int currentUserId;

    public TinNhanAdapter(List<TinNhanModel> list,int currentUserId) {
        this.list = list;
        this.currentUserId = currentUserId;
=======
    private String currentUsername; // Username của người đang đăng nhập

    public TinNhanAdapter(List<TinNhanModel> list, String currentUsername) {
        this.list = list;
        this.currentUsername = currentUsername;
>>>>>>> develop
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
<<<<<<< HEAD
        holder.tvNoiDung.setText(item.getContent());

        // 2.  so sánh: Dùng ID
        // item.getSenderId() lấy từ JSON, currentUserId lấy từ máy
        boolean isMe = (item.getSenderId() != null && item.getSenderId() == currentUserId);

        if (isMe) {
            // ... (Code giao diện bên trong giữ nguyên)
=======

        // Hiển thị nội dung
        holder.tvNoiDung.setText(item.getContent());

        // Xử lý hiển thị: Tin nhắn của mình vs Tin nhắn người khác
        if (item.getSenderUsername() != null && item.getSenderUsername().equals(currentUsername)) {
            // Của mình: Căn phải, đổi màu
>>>>>>> develop
            holder.layoutMsg.setGravity(Gravity.END);
            holder.tvNguoi.setText("Tôi");
            holder.tvNguoi.setTextColor(Color.BLUE);
        } else {
<<<<<<< HEAD
            // ...
            holder.layoutMsg.setGravity(Gravity.START);
            holder.tvNguoi.setText(item.getSenderName());
            holder.tvNguoi.setTextColor(Color.BLACK);
        }
=======
            // Của người khác: Căn trái
            holder.layoutMsg.setGravity(Gravity.START);
            holder.tvNguoi.setText(item.getSenderName()); // Hiện tên đầy đủ
            holder.tvNguoi.setTextColor(Color.BLACK);
        }

        // Hiện thời gian (nếu có)
        // holder.tvTime.setText(item.getSentAt());
>>>>>>> develop
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