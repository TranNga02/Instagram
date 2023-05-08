package com.example.instagram.ui.adapter;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.ui.fragment.blog.BlogFragment;
import com.example.instagram.ui.fragment.post.PostFragment;
import com.example.instagram.ui.model.UserProfile;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class UserAdapterSearch extends RecyclerView.Adapter<UserAdapterSearch.UserViewHolder> {
    private Context context;
    private ArrayList<UserProfile> users;
    private FirebaseUser firebaseUser;
    private UserAdapterSearch.ItemClickListener mListener;
    public UserAdapterSearch(Context context, ArrayList<UserProfile> users, ItemClickListener listener) {
        this.context = context;
        this.users = users;
        this.mListener = listener;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ShapeableImageView img_profile;
        public Button btn_follow;
        public UserViewHolder(@NonNull View itemview) {
            super(itemview);
            username = itemview.findViewById(R.id.cardUserTxtUserName);
            img_profile = itemview.findViewById(R.id.cardUserImgUserPhoto);
        }
        private ItemClickListener itemClickListener; // Khai báo interface

        //Tạo setter cho biến itemClickListenenr
        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
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
        if(user.getAvatar()!=null){
            Glide.with(context).load(user.getAvatar()).into(holder.img_profile);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
