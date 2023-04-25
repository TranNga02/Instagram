package com.example.instagram.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.ui.activity.MainActivity;
import com.example.instagram.ui.fragment.feed.CommentsFragment;
import com.example.instagram.ui.model.PostFeed;
import com.example.instagram.viewmodel.FeedViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class PostAdapterFeed extends RecyclerView.Adapter<PostAdapterFeed.ViewHolder>{
    Context context;
    static ArrayList<PostFeed> postArrayList;
    static String userId;

    public PostAdapterFeed(Context context, ArrayList<PostFeed> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
        this.userId = FirebaseAuth.getInstance().getUid();
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
        holder.like = post.isLike(userId);
        if(holder.like){
            holder.btnLike.setImageResource(R.drawable.ic_heart_select);
        }else{
            holder.btnLike.setImageResource(R.drawable.ic_heart);
        }

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

        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.like = !holder.like;
                if(holder.like){
                    holder.btnLike.setImageResource(R.drawable.ic_heart_select);
                }else{
                    holder.btnLike.setImageResource(R.drawable.ic_heart);
                }

                int position = holder.getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    FeedViewModel model = new FeedViewModel();
                    String postId = postArrayList.get(position).getId();
                    model.updateLikeOfPost(postId, userId);
                }
            }
        });

        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentsFragment commentsFragment = new CommentsFragment();
                Bundle bundle = new Bundle();
                String postId = postArrayList.get(holder.getAdapterPosition()).getId(); // Lấy post_id từ dữ liệu của bạn
                bundle.putString("post-id", postId);
                commentsFragment.setArguments(bundle);
                FragmentTransaction transaction = ((MainActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView, commentsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivPost, btnLike, btnComment, ivUserAvatar;
        public TextView tvDate, tvUsername, tvLikes, tvPostContent;
        boolean like = true;

        public ViewHolder(View view) {
            super(view);
            ivPost = view.findViewById(R.id.vv_post);
            tvDate = view.findViewById(R.id.tv_date);
            tvUsername = view.findViewById(R.id.tv_username);
            tvLikes = view.findViewById(R.id.tv_likes);
            tvPostContent = view.findViewById(R.id.tv_post_content);
            ivUserAvatar = view.findViewById(R.id.iv_user_avatar);
            btnLike = view.findViewById(R.id.btn_like);
            btnComment = view.findViewById(R.id.btn_comment);
        }
    }
}
