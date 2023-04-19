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
import com.example.instagram.ui.model.PostFeed;

import java.util.ArrayList;

public class PostAdapterFeed extends RecyclerView.Adapter<PostAdapterFeed.ViewHolder>{
    Context context;
    ArrayList<PostFeed> postArrayList;

    public PostAdapterFeed(Context context, ArrayList<PostFeed> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapterFeed.ViewHolder holder, int position) {
        PostFeed post = postArrayList.get(position);

        holder.tvDate.setText(String.valueOf(post.getNumberofDays()));
        holder.tvLikes.setText(String.valueOf(post.countLikes()));
        holder.tvPostContent.setText(post.getContent());
        holder.tvUsername.setText(post.getUsername());

        if (post.getSrc().get(0) != null) {
            Uri imageUri = Uri.parse(post.getSrc().get(0));
            Glide.with(holder.itemView.getContext())
                    .load(imageUri)
                    .into(holder.ivPost);
        }


        if (post.getAvatar() != null) {
            Uri avatarUri = Uri.parse(post.getAvatar());
            Glide.with(holder.itemView.getContext())
                    .load(avatarUri)
                    .into(holder.ivUserAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivPost;
        public TextView tvDate, tvUsername, tvLikes, tvPostContent;
        public ImageView ivUserAvatar;

        public ViewHolder(View view) {
            super(view);
            ivPost = view.findViewById(R.id.vv_post);
            tvDate = view.findViewById(R.id.tv_date);
            tvUsername = view.findViewById(R.id.tv_username);
            tvLikes = view.findViewById(R.id.tv_likes);
            tvPostContent = view.findViewById(R.id.tv_post_content);
            ivUserAvatar = view.findViewById(R.id.iv_user_avatar);
        }
    }
}
