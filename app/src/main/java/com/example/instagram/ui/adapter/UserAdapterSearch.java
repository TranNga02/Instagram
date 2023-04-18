package com.example.instagram.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.ui.model.UserProfile;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class UserAdapterSearch extends RecyclerView.Adapter<UserAdapterSearch.UserViewHolder> {
    private Context context;
    private List<UserProfile> users;
    private FirebaseUser firebaseUser;

    public UserAdapterSearch(Context context, List<UserProfile> users) {
        this.context = context;
        this.users = users;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ShapeableImageView img_profile;
        public Button btn_follow;
        public UserViewHolder(@NonNull View itemview) {
            super(itemview);
            username = itemview.findViewById(R.id.cardUserTxtUserName);
            img_profile = itemview.findViewById(R.id.cardUserImgUserPhoto);
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.card_user,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final UserProfile user = users.get(position);
        holder.username.setText(user.getUsername());
        Glide.with(context).load(user.getAvatar()).into(holder.img_profile);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
