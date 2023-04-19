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

import java.util.List;

public class UserAdapterSearch extends RecyclerView.Adapter<UserAdapterSearch.UserViewHolder> {
    private Context context;
    private List<UserProfile> users;
    private FirebaseUser firebaseUser;

    public UserAdapterSearch(Context context, List<UserProfile> users) {
        this.context = context;
        this.users = users;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        public TextView username;
        public ShapeableImageView img_profile;
        public Button btn_follow;
        public UserViewHolder(@NonNull View itemview) {
            super(itemview);
            username = itemview.findViewById(R.id.cardUserTxtUserName);
            img_profile = itemview.findViewById(R.id.cardUserImgUserPhoto);
            itemView.setOnClickListener(this); // Mấu chốt ở đây , set sự kiên onClick cho View
            itemView.setOnLongClickListener(this); // Mấu chốt ở đây , set sự kiên onLongClick cho View
        }
        private ItemClickListener itemClickListener; // Khai báo interface

        //Tạo setter cho biến itemClickListenenr
        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true); // Gọi interface , true là vì đây là onLongClick
            return true;
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
//        holder.setItemClickListener(new ItemClickListener() {
//            @Override
//            public void onClick(View view, int position, boolean isLongClick) {
//                if(isLongClick)
//                    Toast.makeText(context, "Long Click: ", Toast.LENGTH_SHORT).show();
//                else{
//                    ((FragmentActivity) view.getContext()).getFragmentManager().beginTransaction()
//                            .replace(R.id.fragmentContainerView, )
//                            .commit();
//                }
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    public interface ItemClickListener {
        void onClick(View view, int position,boolean isLongClick);
    }
}
