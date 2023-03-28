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
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapterSearch extends RecyclerView.Adapter<UserAdapterSearch.UserViewHolder> {
    private Context context;
    private List<User> users;

    public UserAdapterSearch(Context context, List<User> users) {
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
//        SearchFragmentDirections.SearchToProfile searchToProfile = SearchFragmentDirections.searchToProfile();
//        searchToProfile.setUserId(user_id);
//
//        Navigation.findNavController(view).navigate(searchToProfile);
//    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardUserBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.card_user, parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);

        holder.binding.setUserAdapterSearch(this);
        holder.binding.setUser(user);

        // load photo
        Picasso.get().load(user.getUser_photo()).into(holder.binding.cardUserImgUserPhoto);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
