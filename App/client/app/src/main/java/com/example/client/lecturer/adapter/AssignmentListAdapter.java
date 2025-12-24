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

    public AssignmentListAdapter(List<AssignmentDTO> list) {
        this.assignmentList = list;
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
            dueDate = dueDate.replace("T", " "); // Chuyển 2025-12-24T23:59:00 thành 2025-12-24 23:59:00
        }
        holder.tvDueDate.setText("Hạn nộp: " + (dueDate != null ? dueDate : "Không có hạn"));
    }

    @Override
    public int getItemCount() {
        return assignmentList != null ? assignmentList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvClassName, tvDueDate;

        public ViewHolder(View itemView) {
            super(itemView);
            // Ánh xạ đúng các ID trong file item_assignment_lecturer.xml
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvClassName = itemView.findViewById(R.id.tv_class_name);
            tvDueDate = itemView.findViewById(R.id.tv_due_date);
        }
    }
}