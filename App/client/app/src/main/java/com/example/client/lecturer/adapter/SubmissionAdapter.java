package com.example.client.lecturer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.client.R;
import com.example.client.lecturer.model.SubmissionDTO;
import java.util.List;

public class SubmissionAdapter extends RecyclerView.Adapter<SubmissionAdapter.SubmissionViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(SubmissionDTO submission);
    }

    private Context context;
    private List<SubmissionDTO> submissions;
    private OnItemClickListener listener;

    public SubmissionAdapter(Context context, List<SubmissionDTO> submissions, OnItemClickListener listener) {
        this.context = context;
        this.submissions = submissions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubmissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lecturer_item_student_submission, parent, false);
        return new SubmissionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubmissionViewHolder holder, int position) {
        SubmissionDTO s = submissions.get(position);
        holder.tvName.setText(s.getStudentName());
        holder.tvId.setText("ID: " + s.getStudentId());
        holder.tvGrade.setText("Điểm: " + (s.getGrade() != null ? s.getGrade() : "Chưa có"));

        holder.itemView.setOnClickListener(v -> listener.onItemClick(s));
    }

    @Override
    public int getItemCount() {
        return submissions.size();
    }

    static class SubmissionViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvId, tvGrade;

        public SubmissionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvStudentName);
            tvId = itemView.findViewById(R.id.tvStudentId);
            tvGrade = itemView.findViewById(R.id.tvGrade);
        }
    }
}
