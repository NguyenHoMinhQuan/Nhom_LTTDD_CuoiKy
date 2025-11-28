package com.example.client.lecturer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.lecturer.ScheduleItem;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private final List<ScheduleItem> scheduleList;
    private final OnItemClickListener listener;

    // Định nghĩa Interface để xử lý sự kiện click (giống như Adapter trước)
    public interface OnItemClickListener {
        void onItemClick(ScheduleItem item);
    }

    // Constructor nhận danh sách dữ liệu và listener
    public ScheduleAdapter(List<ScheduleItem> scheduleList, OnItemClickListener listener) {
        this.scheduleList = scheduleList;
        this.listener = listener;
    }

    // 1. Tạo ViewHolder mới (inflate layout item_giangvien_timetable_layout.xml)
    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lecturer_item_timetable, parent, false);
        return new ScheduleViewHolder(view);
    }

    // 2. Liên kết dữ liệu với View (Binding data)
    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        ScheduleItem currentItem = scheduleList.get(position);

        // Đặt dữ liệu vào các TextView dựa trên ID trong layout item
        holder.dayOfWeekTextView.setText(currentItem.getDayOfWeek());
        holder.courseNameTextView.setText(currentItem.getCourseName());
        holder.startTimeTextView.setText("Bắt đầu: " + currentItem.getStartTime());
        holder.endTimeTextView.setText("Kết thúc: " + currentItem.getEndTime());
        holder.roomTextView.setText("Phòng: " + currentItem.getRoom());

        // Xử lý sự kiện click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(currentItem);
            }
        });
    }

    // 3. Trả về số lượng mục
    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    // Lớp ViewHolder: Giữ references đến các View trong mỗi item
    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        final TextView dayOfWeekTextView;
        final TextView courseNameTextView;
        final TextView startTimeTextView;
        final TextView endTimeTextView;
        final TextView roomTextView;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các id từ item_giangvien_timetable_layout.xml
            dayOfWeekTextView = itemView.findViewById(R.id.tv_day_of_week);
            courseNameTextView = itemView.findViewById(R.id.tv_course_name);
            startTimeTextView = itemView.findViewById(R.id.tv_start_time);
            endTimeTextView = itemView.findViewById(R.id.tv_end_time);
            roomTextView = itemView.findViewById(R.id.tv_room);
        }
    }
}
