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
        holder.tvDate.setText(post.getTime());
        holder.tvLikes.setText(String.valueOf(post.countLikes()));
        holder.tvPostContent.setText(post.getContent());


        Uri imageUri = Uri.parse(post.getSrc().get(0));

        Glide.with(holder.itemView.getContext())
                .load(imageUri)
                .into(holder.vvPost);
//
//        holder.vvPost.setVideoURI(videoUri);
//        holder.vvPost.start();
//        holder.vvPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (holder.vvPost.isPlaying()) {
//                    holder.vvPost.pause();
//                } else {
//                    holder.vvPost.start();
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView vvPost;
        public TextView tvDate, tvUsername, tvLikes, tvPostContent;
        public ImageView ivUserAvatar;

        public ViewHolder(View view) {
            super(view);
            vvPost = view.findViewById(R.id.vv_post);
            tvDate = view.findViewById(R.id.tv_date);
            tvUsername = view.findViewById(R.id.tv_username);
            tvLikes = view.findViewById(R.id.tv_likes);
            tvPostContent = view.findViewById(R.id.tv_post_content);
            ivUserAvatar = view.findViewById(R.id.iv_user_avatar);
        }
    }

}
