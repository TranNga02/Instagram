package com.example.instagram.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.databinding.CardUserBinding;
import com.example.instagram.ui.model.User;
import com.example.instagram.ui.model.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapterFollow extends RecyclerView.Adapter<UserAdapterFollow.UserViewHolder> {
    private Context context;
    private List<UserProfile> users;

    public UserAdapterFollow(Context context, List<UserProfile> users) {
        this.context = context;
        this.users = users;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public CardUserBinding binding;

        public UserViewHolder(@NonNull CardUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

//    public void navToUserProfile(View view, int user_id) {
//        FollowFragmentDirections.FollowToProfile followToProfile = FollowFragmentDirections.followToProfile();
//        followToProfile.setUserId(user_id);
//
//        Navigation.findNavController(view).navigate(followToProfile);
//    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardUserBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.card_user, parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserProfile user = users.get(position);

        holder.binding.setUserAdapterFollow(this);
        holder.binding.setUser(user);



    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
