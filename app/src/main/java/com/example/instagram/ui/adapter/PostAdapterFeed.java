package com.example.instagram.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.repository.UserRepository;
import com.example.instagram.ui.model.PostFeed;
import com.example.instagram.ui.model.UserProfile;
import com.example.instagram.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

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
        System.out.println("-------------------------------"+post.getUserId());
        UserViewModel userViewModel = new UserViewModel();

        holder.tvDate.setText(String.valueOf(post.getNumberofDays()));
        holder.tvLikes.setText(String.valueOf(post.countLikes()));
        holder.tvPostContent.setText(post.getContent());
        Uri imageUri = Uri.parse(post.getSrc().get(0));
        Glide.with(holder.itemView.getContext())
                .load(imageUri)
                .into(holder.ivPost);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            CompletableFuture<Void> user = getUserById(post.getUserId())
                    .thenAccept(userPro -> {

                        // Các lệnh đằng sau sẽ được thực hiện sau khi getUserById hoàn thành
                        holder.tvUsername.setText(userPro.getUsername());
                        Uri avatarUri = Uri.parse(userPro.getAvatar());
                        Glide.with(holder.itemView.getContext())
                                .load(avatarUri)
                                .into(holder.ivUserAvatar);

                    })
                    .exceptionally(ex -> {
                        // Xử lý nếu có lỗi xảy ra trong quá trình getUserById
                        System.out.println("Error: " + ex.getMessage());
                        return null;
                    });
        }


//
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

    public CompletableFuture<UserProfile> getUserById(String userId) {

        CompletableFuture<UserProfile> completableFuture = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            completableFuture = new CompletableFuture<>();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CompletableFuture<UserProfile> finalCompletableFuture = completableFuture;
        db.collection("profiles")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        UserProfile user = new UserProfile();
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            if (userId != null && userId.equals(document.getId())) {
                                user = document.toObject(UserProfile.class);
                            }
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            finalCompletableFuture.complete(user);
                        }
                    } else {
                        System.out.println("Error getting documents." + task.getException());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            finalCompletableFuture.completeExceptionally(task.getException());
                        }
                    }
                });

        return completableFuture;
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
