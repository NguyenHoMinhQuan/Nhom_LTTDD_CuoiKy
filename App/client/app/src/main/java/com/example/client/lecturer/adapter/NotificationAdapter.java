package com.example.client.lecturer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.lecturer.model.NotificationItem;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final List<NotificationItem> notificationList;
    private final OnItemClickListener listener;

    // Interface để xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(NotificationItem item);
    }

    public NotificationAdapter(List<NotificationItem> notificationList, OnItemClickListener listener) {
        this.notificationList = notificationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng layout item thông báo của bạn
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lecturer_item_announcement, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationItem currentItem = notificationList.get(position);

        // Gán dữ liệu thực tế từ Backend
        holder.titleTextView.setText(currentItem.getTitle());
        holder.summaryTextView.setText(currentItem.getBody());

        // Nếu layout của bạn có trường hiển thị thời gian (ví dụ tv_announcement_time)
        if (holder.timeTextView != null && currentItem.getCreatedAt() != null) {
            holder.timeTextView.setText(currentItem.getCreatedAt());
        }

        // Xử lý sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }

    // Lớp ViewHolder để giữ các tham chiếu View
    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        final TextView titleTextView;
        final TextView summaryTextView;
        TextView timeTextView; // Có thể để null nếu layout không có

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ ID từ file xml của bạn (lecturer_item_announcement.xml)
            titleTextView = itemView.findViewById(R.id.tv_announcement_title);
            summaryTextView = itemView.findViewById(R.id.tv_announcement_summary);
        }
    }
}