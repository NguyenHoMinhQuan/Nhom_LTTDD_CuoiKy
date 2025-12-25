package com.example.client.lecturer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.lecturer.model.ChatMessageDTO;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ME = 1;
    private static final int TYPE_OTHER = 2;
    private List<ChatMessageDTO> messages;
    private int currentUserId; // ID của giảng viên đang dùng app

    public ChatAdapter(List<ChatMessageDTO> messages, int currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getSenderId() == currentUserId) return TYPE_ME;
        return TYPE_OTHER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ME) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_me, parent, false);
            return new MeViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_other, parent, false);
            return new OtherViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessageDTO msg = messages.get(position);
        if (holder instanceof MeViewHolder) {
            ((MeViewHolder) holder).tvMessage.setText(msg.getContent());
        } else {
            ((OtherViewHolder) holder).tvMessage.setText(msg.getContent());
            ((OtherViewHolder) holder).tvSender.setText(msg.getSenderName());
        }
    }

    @Override
    public int getItemCount() { return messages.size(); }

    static class MeViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        MeViewHolder(View v) { super(v); tvMessage = v.findViewById(R.id.tv_message_me); }
    }

    static class OtherViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvSender;
        OtherViewHolder(View v) {
            super(v);
            tvMessage = v.findViewById(R.id.tv_message_other);
            tvSender = v.findViewById(R.id.tv_sender_name);
        }
    }
}
