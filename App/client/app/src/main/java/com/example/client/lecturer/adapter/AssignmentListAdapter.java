package com.example.client.lecturer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.client.R;
import com.example.client.lecturer.model.AssignmentDTO;
import java.util.List;

public class AssignmentListAdapter extends RecyclerView.Adapter<AssignmentListAdapter.ViewHolder> {
    private List<AssignmentDTO> assignmentList;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AssignmentDTO assignment);
    }

    public AssignmentListAdapter(List<AssignmentDTO> list, OnItemClickListener listener) {
        this.assignmentList = list;
        this.listener = listener;
    }

    public void updateData(List<AssignmentDTO> newList) {
        this.assignmentList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lecturer_item_assignment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AssignmentDTO item = assignmentList.get(position);

        holder.tvTitle.setText(item.getTitle());
        holder.tvClassName.setText("Lớp ID: " + item.getClassId());

        String dueDate = item.getDueDate();
        if (dueDate != null && dueDate.contains("T")) {
            // Chuyển đổi định dạng ISO từ server (2025-12-24T23:59:00) thành dạng dễ đọc hơn
            dueDate = dueDate.replace("T", " ");
        }
        holder.tvDueDate.setText("Hạn nộp: " + (dueDate != null ? dueDate : "Không có hạn"));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assignmentList != null ? assignmentList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvClassName, tvDueDate;

        public ViewHolder(View itemView) {
            super(itemView);
            // Ánh xạ đúng các ID trong file lecturer_item_assignment.xml
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvClassName = itemView.findViewById(R.id.tv_class_name);
            tvDueDate = itemView.findViewById(R.id.tv_due_date);
        }
    }
}