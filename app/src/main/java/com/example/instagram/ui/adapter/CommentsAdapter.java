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
import com.example.instagram.ui.model.Comment;
import com.example.instagram.viewmodel.CaculateTime;
import com.example.instagram.viewmodel.CommentViewModel;
import com.example.instagram.viewmodel.FeedViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{
    Context context;
    static ArrayList<Comment> commentArrayList;
    static String userId;

    public CommentsAdapter(Context context, ArrayList<Comment> commentArrayList) {
        this.context = context;
        this.commentArrayList = commentArrayList;
        this.userId = FirebaseAuth.getInstance().getUid();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_comment, parent, false);
        return new CommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
        Comment comment = commentArrayList.get(position);

        holder.tvDate.setText(CaculateTime.getInstance().caculateTime(comment.getTime()));
        holder.tvUsername.setText(comment.getUsername());
        holder.tvContent.setText(comment.getContent());
        holder.like = comment.isLike(userId);
        if(comment.countLikes()>0){
            holder.tvLikes.setText(String.valueOf(comment.countLikes())+" lượt thích");
        }
        else{
            holder.tvLikes.setText("");
        }

        if(holder.like){
            holder.btnLike.setImageResource(R.drawable.ic_heart_select);
        }else{
            holder.btnLike.setImageResource(R.drawable.ic_heart);
        }

        if (comment.getAvatar() != null) {
            Uri avatarUri = Uri.parse(comment.getAvatar());
            Glide.with(holder.itemView.getContext())
                    .load(avatarUri)
                    .into(holder.ivUserAvatar);
        }

        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    CommentViewModel model = new CommentViewModel();
                    String commentId = commentArrayList.get(position).getId();
                    model.updateLikeOfComment(commentId, userId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvContent, tvLikes, tvReply, tvDate;
        ImageView ivUserAvatar, btnLike;
        boolean like = true;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvUsername = view.findViewById(R.id.tv_username);
            tvContent = view.findViewById(R.id.tv_content);
            tvLikes = view.findViewById(R.id.tv_likes);
            tvReply = view.findViewById(R.id.tv_reply);
            tvDate = view.findViewById(R.id.tv_date);
            ivUserAvatar = view.findViewById(R.id.iv_user_avatar);
            btnLike = view.findViewById(R.id.btn_like);
        }
    }

}
