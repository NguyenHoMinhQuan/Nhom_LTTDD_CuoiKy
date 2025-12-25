package com.example.client.lecturer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.client.R;
import com.example.client.lecturer.model.ClassDTO;
import java.util.List;

public class ChatClassAdapter extends RecyclerView.Adapter<ChatClassAdapter.ViewHolder> {
    private List<ClassDTO> classList;
    private OnClassClickListener listener;

    public interface OnClassClickListener {
        void onClassClick(ClassDTO classItem);
    }

    public ChatClassAdapter(List<ClassDTO> classList, OnClassClickListener listener) {
        this.classList = classList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_class, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClassDTO item = classList.get(position);
        holder.tvClassName.setText(item.getClassCode()); // Giả định ClassDTO có getClassName()
        holder.itemView.setOnClickListener(v -> listener.onClassClick(item));
    }

    @Override
    public int getItemCount() {
        return classList != null ? classList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvClassName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClassName = itemView.findViewById(R.id.tv_class_name);
        }
    }
}