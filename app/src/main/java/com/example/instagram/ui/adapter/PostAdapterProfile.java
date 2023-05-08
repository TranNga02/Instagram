package com.example.instagram.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.databinding.CardPostThumbnailBinding;
import com.example.instagram.ui.model.Post;
import com.example.instagram.ui.model.PostFeed;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostAdapterProfile extends RecyclerView.Adapter<PostAdapterProfile.ViewHolder> {
    private Context context;
    private List<PostFeed> posts;
    private OnItemClickListener mListener;
    public PostAdapterProfile(Context context, ArrayList<PostFeed> posts, OnItemClickListener listener) {
        this.context = context;
        this.posts = posts;
        mListener = listener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView post_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post_image = itemView.findViewById(R.id.cardPostThumbnailImgPostPhoto);
        }
    }


//    public void navToPostDetails(View view, int post_id) {
//        ProfileFragmentDirections.ProfileToPostDetails profileToPostDetails = ProfileFragmentDirections.profileToPostDetails();
//        profileToPostDetails.setPostId(post_id);
//
//        Navigation.findNavController(view).navigate(profileToPostDetails);
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_post_thumbnail, parent, false);
        return new PostAdapterProfile.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        PostFeed post = posts.get(i);
//        UserProfile user = userViewModel.getUserById(post.getUserId());

//        holder.tvUsername.setText(user.getUsername());

        Uri imageUri = Uri.parse(post.getSrc().get(0));
        Glide.with(holder.itemView.getContext())
                .load(imageUri)
                .into(holder.post_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(i);
            }
        });
    }

    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Post post = posts.get(position);
//
//        holder.binding.setPostAdapterProfile(this);
//        holder.binding.setPost(post);
//
//        // load photo
//        Glide.with(context).load(post.getPost_photo()).into(holder.binding.cardPostThumbnailImgPostPhoto);
//        Toast.makeText(context, posts.get(position).getPost_photo(), Toast.LENGTH_SHORT).show();
//    }

    public int getItemCount() {
        return posts.size();
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
