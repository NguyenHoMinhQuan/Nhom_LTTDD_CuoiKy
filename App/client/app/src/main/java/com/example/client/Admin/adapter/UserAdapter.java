package com.example.client.Admin.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.client.R;
import com.example.client.api.AdminResponse;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<AdminResponse.User> mListUser;
    private IClickItemUser iClickItemUser;

    public interface IClickItemUser {
        void onClickItemUser(AdminResponse.User user);
    }

    public UserAdapter(Context context, List<AdminResponse.User> list, IClickItemUser listener) {
        this.mListUser = list;
        this.iClickItemUser = listener;
    }

    // FIX LỖI: Thêm phương thức setData
    public void setData(List<AdminResponse.User> list) {
        this.mListUser = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Đảm bảo file layout là item_row_user.xml (hoặc item_user.xml tùy bạn đặt)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        AdminResponse.User user = mListUser.get(position);
        if (user == null) return;

        holder.tvId.setText("#" + user.id);
        holder.tvUsername.setText(user.username); // Hiển thị username
        holder.tvName.setText(user.fullName);
        holder.tvEmail.setText(user.email);
        holder.tvRole.setText(user.role);
        holder.tvDept.setText(user.department != null ? user.department : "-");

        boolean active = Boolean.TRUE.equals(user.isActive);
        holder.viewStatusColor.setBackgroundTintList(
                ColorStateList.valueOf(active ? Color.parseColor("#4CAF50") : Color.parseColor("#F44336")));

        holder.itemView.setOnClickListener(v -> iClickItemUser.onClickItemUser(user));
    }

    @Override
    public int getItemCount() {
        return mListUser != null ? mListUser.size() : 0;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvUsername, tvName, tvEmail, tvRole, tvDept;
        View viewStatusColor;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvUsername = itemView.findViewById(R.id.tvUsername); // Ánh xạ ID username
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvDept = itemView.findViewById(R.id.tvDept);
            viewStatusColor = itemView.findViewById(R.id.viewStatusColor);
        }
    }
}