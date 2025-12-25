package com.example.client.Admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.api.AdminResponse; // Import class gộp

import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    // SỬA: Dùng AdminResponse.Announcement
    private List<AdminResponse.Announcement> mList;
    private final IClickListener listener;

    public interface IClickListener {
        void onClick(AdminResponse.Announcement item);
    }

    public AnnouncementAdapter(List<AdminResponse.Announcement> list, IClickListener listener) {
        this.mList = list;
        this.listener = listener;
    }

    public void setData(List<AdminResponse.Announcement> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_announcement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdminResponse.Announcement item = mList.get(position);

        holder.tvTitle.setText(item.title);
        holder.tvBody.setText(item.body);

        // Cắt chuỗi ngày cho gọn nếu cần
        holder.tvDate.setText(item.createdAt);

        holder.itemView.setOnClickListener(v -> listener.onClick(item));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvBody, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Đảm bảo item_row_announcement.xml có các ID này
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}