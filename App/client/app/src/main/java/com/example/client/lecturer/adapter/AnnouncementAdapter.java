package com.example.client.lecturer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.lecturer.Announcement;

import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder> {

    private final List<Announcement> announcementList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Announcement announcement);
    }

    // Constructor để nhận danh sách dữ liệu
    public AnnouncementAdapter(List<Announcement> announcementList, OnItemClickListener listener) {
        this.announcementList = announcementList;
        this.listener = listener;
    }

    // 1. Tạo ViewHolder mới (inflate layout item)
    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Ánh xạ layout item_recent_announcement.xml vào View
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lecturer_item_announcement, parent, false);
        return new AnnouncementViewHolder(view);
    }

    // 2. Liên kết dữ liệu với View (Binding data)
    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        Announcement currentAnnouncement = announcementList.get(position);

        holder.titleTextView.setText(currentAnnouncement.getTitle());
        holder.summaryTextView.setText(currentAnnouncement.getSummary());

        // Gán sự kiện click cho toàn bộ View của item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức onItemClick của Listener
                listener.onItemClick(currentAnnouncement);
            }
        });
    }

    // 3. Trả về số lượng mục trong danh sách
    @Override
    public int getItemCount() {
        return announcementList.size();
    }

    // Lớp ViewHolder: Giữ references đến các View trong mỗi item
    public static class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        final TextView titleTextView;
        final TextView summaryTextView;
        // Bạn có thể thêm ImageView nếu muốn sử dụng nó

        public AnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các id từ item_announcement.xml
            titleTextView = itemView.findViewById(R.id.tv_announcement_title);
            summaryTextView = itemView.findViewById(R.id.tv_announcement_summary);
        }
    }
}