package com.example.instagram.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.databinding.CardPostThumbnailBinding;
import com.example.instagram.ui.model.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostAdapterProfile extends RecyclerView.Adapter<PostAdapterProfile.PostThumbnailViewHolder> {
    private Context context;
    private List<Post> posts;

    public PostAdapterProfile( ArrayList<Post> posts) {
//        this.context = context;
        this.posts = posts;
    }

    public class PostThumbnailViewHolder extends RecyclerView.ViewHolder {
        public CardPostThumbnailBinding binding;

        public PostThumbnailViewHolder(@NonNull CardPostThumbnailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

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
    public PostThumbnailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardPostThumbnailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.card_post_thumbnail, parent, false);
        return new PostThumbnailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostThumbnailViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.binding.setPostAdapterProfile(this);
        holder.binding.setPost(post);

        // load photo
        Picasso.get().load(posts.get(position).getPost_photo()).into(holder.binding.cardPostThumbnailImgPostPhoto);
        Toast.makeText(context, posts.get(position).getPost_photo(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
