package com.example.instagram.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.ui.model.Notification;
import com.example.instagram.viewmodel.CaculateTime;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {
    Context context;
    static ArrayList<Notification> notificationArrayList;
    static String userId;

    public NotificationsAdapter(Context context, ArrayList<Notification> notificationArrayList) {
        this.context = context;
        this.notificationArrayList = notificationArrayList;
        this.userId = FirebaseAuth.getInstance().getUid();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_notification, parent, false);
        return new NotificationsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ViewHolder holder, int position) {
        Notification notification = notificationArrayList.get(position);

        holder.tvContent.setText(notification.getContent());
        holder.tvDate.setText(CaculateTime.getInstance().caculateTime(notification.getTime()));
        if (notification.getAvatar() != null) {
            Uri avatarUri = Uri.parse(notification.getAvatar());
            Glide.with(holder.itemView.getContext())
                    .load(avatarUri)
                    .into(holder.ivUserAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return notificationArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivUserAvatar;
        TextView tvContent, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}
