package com.example.client.HocVien;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.client.HocVien.Models.TinNhanModel;
import com.example.client.R;
import java.util.List;

public class TinNhanAdapter extends RecyclerView.Adapter<TinNhanAdapter.ViewHolder> {
    List<TinNhanModel> list;
    public TinNhanAdapter(List<TinNhanModel> list) { this.list = list; }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hocvien_item_tin_nhan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TinNhanModel item = list.get(position);
        holder.tvNoiDung.setText(item.getNoiDung());
        holder.tvNguoi.setText("User: " + item.getNguoiGuiId());
    }

    @Override public int getItemCount() { return list.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNguoi, tvNoiDung;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNguoi = itemView.findViewById(R.id.tvNguoiGui);
            tvNoiDung = itemView.findViewById(R.id.tvNoiDung);
        }
    }
}