package com.example.client.Admin.adapter; // Hoặc package của bạn

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.api.AdminResponse; // Import đúng đường dẫn model

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private List<AdminResponse.ClassRow> mList;
    private final IClickItemListener iClickItemListener;

    // Interface để xử lý sự kiện click ra ngoài Activity
    public interface IClickItemListener {
        void onClickItem(AdminResponse.ClassRow item);
    }

    public ClassAdapter(List<AdminResponse.ClassRow> mList, IClickItemListener listener) {
        this.mList = mList;
        this.iClickItemListener = listener;
    }

    // Cập nhật dữ liệu mới mà không cần tạo lại Adapter
    public void setData(List<AdminResponse.ClassRow> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Ánh xạ file XML giao diện của 1 dòng (item_row_schedule.xml hoặc đổi thành item_row_class.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_schedule_layout, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        AdminResponse.ClassRow item = mList.get(position);
        if (item == null) return;

        // 1. Hiển thị Tên Môn
        holder.tvTenMon.setText(item.tenMon != null ? item.tenMon : "Môn học chưa đặt tên");

        // 2. Hiển thị Lớp + Giảng viên
        String thongTinLop = "Lớp: " + (item.maLop != null ? item.maLop : "") +
                " - GV: " + (item.giangVien != null ? item.giangVien : "Chưa có");
        holder.tvLop.setText(thongTinLop);

        // 3. Hiển thị Phòng
        holder.tvPhong.setText("Phòng: " + (item.phong != null ? item.phong : "N/A"));

        // 4. Hiển thị Giờ (Cắt chuỗi HH:mm)
        String gioBD = (item.gioBD != null && item.gioBD.length() >= 5) ? item.gioBD.substring(0, 5) : "--:--";
        String gioKT = (item.gioKT != null && item.gioKT.length() >= 5) ? item.gioKT.substring(0, 5) : "--:--";
        holder.tvThoiGian.setText("🕒 " + gioBD + " - " + gioKT);

        // 5. Bắt sự kiện Click vào dòng -> Gửi item ra ngoài Activity để fillForm
        holder.itemView.setOnClickListener(v -> iClickItemListener.onClickItem(item));
        holder.itemView.setOnClickListener(v -> {
            if (iClickItemListener != null) {
                iClickItemListener.onClickItem(item); // Gửi tín hiệu ra Activity
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    // ViewHolder: Ánh xạ các View trong item_row_schedule.xml
    public static class ClassViewHolder extends RecyclerView.ViewHolder {

        TextView tvTenMon, tvThoiGian, tvLop, tvPhong;
        View vIndicator;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);

            // Đảm bảo các ID này KHỚP 100% với file XML bạn đã gửi
            tvTenMon = itemView.findViewById(R.id.tvTenMon);
            tvThoiGian = itemView.findViewById(R.id.tvThoiGian);
            tvLop = itemView.findViewById(R.id.tvLop);
            tvPhong = itemView.findViewById(R.id.tvPhong);
            vIndicator = itemView.findViewById(R.id.vIndicator); // View màu đỏ bên trái (nếu đã thêm ID)
        }
    }
}