package com.example.instagram.ui.fragment.blog;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.ui.adapter.PostAdapterProfile;
import com.example.instagram.ui.fragment.setting.SettingFragment;
import com.example.instagram.ui.model.Post;
import com.example.instagram.ui.model.PostFeed;
import com.example.instagram.ui.model.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.net.IDN;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BlogFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences preferences = getContext().getSharedPreferences("PREPS", Context.MODE_PRIVATE);
        profileId = preferences.getString("profileID","none");
        avatar = view.findViewById(R.id.frgProfileImgUserPhoto);
        txt_followers = view.findViewById(R.id.txt_lbl_followers);
        txt_followings = view.findViewById(R.id.txt_lbl_followings);
        txt_post = view.findViewById(R.id.txt_lbl_post);
        fullname = view.findViewById(R.id.frgProfileLblUserName);
        bio = view.findViewById(R.id.frgProfileLblUserBio);
        setting = view.findViewById(R.id.btn_setting);
        recyclerViewPost = view.findViewById(R.id.frgProfileRecyclerView);
        recyclerViewPost.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerViewPost.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<PostFeed>();
        postAdapterProfile = new PostAdapterProfile(getContext(), postList, new PostAdapterProfile.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String idPost = String.valueOf(postList.get(position).getId());

                Bundle bundle = new Bundle();
                bundle.putString("idPost", idPost);
                NavController navController = Navigation.findNavController(getActivity(), R.id.fragmentContainerView);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.blogFragment, true)
                        .build();
                navController.navigate(R.id.detailPostFragment, bundle, navOptions);
            }
        });
        recyclerViewPost.setAdapter(postAdapterProfile);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = setting.getText().toString();
                if(btn.equals("Setting Profile")){
                    NavController navController = Navigation.findNavController(getActivity(), R.id.fragmentContainerView);
                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.blogFragment, true)
                            .build();
                    navController.navigate(R.id.settingFragment, null, navOptions);
                }
                else if (btn.equals("Follow")) {
                    follow(idUser);
                }
                else {
                    unfollow(idUser);
                }
            }
        });
        Bundle bundle2 = getArguments();
        if (bundle2 != null) {
            emailUser = bundle2.getString("email");
            idUser = bundle2.getString("idUser");
        }
        ShowUser(emailUser, idUser);
        return view;
    }

    ShapeableImageView avatar;
    TextView  txt_post, txt_followers, txt_followings, fullname, bio;
    Button setting;
    RecyclerView recyclerViewPost;
    FirebaseUser firebaseUser;
    String profileId;
    String emailUser = "";
    String idUser = "";
    private ArrayList<PostFeed> postList;
    PostAdapterProfile postAdapterProfile;
    public void ShowUser(String emailUser, String idUser){
        postList.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String postId = document.getId();
                        PostFeed postFeed = document.toObject(PostFeed.class); // create a new Post instance using the data from the document
                        if(idUser == ""){
                            if(firebaseUser.getUid().equals(postFeed.getUserId())) {
                                postFeed.setId(postId);
                                postList.add(postFeed);
                            }
                        }
                        else {
                            if(idUser.equals(postFeed.getUserId())) {
                                postFeed.setId(postId);
                                postList.add(postFeed);
                            }
                        }
                    }
                    postAdapterProfile.notifyDataSetChanged();
                    txt_post.setText(String.valueOf(postList.size()));

                }
                else {

                }
            }
        });
        db.collection("profiles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        UserProfile user = document.toObject(UserProfile.class); // create a new Post instance using the data from the document
                        if(idUser == "" || idUser.equals(firebaseUser.getUid())){
                            if(firebaseUser.getEmail().equals(user.getEmail())) {
                                txt_followers.setText(String.valueOf(user.getFollower().size()));
                                txt_followings.setText(String.valueOf(user.getFollowed().size()));
                                fullname.setText(user.getFullname().toString());
                                bio.setText(user.getBio().toString());
                                txt_post.setText(String.valueOf(postAdapterProfile.getItemCount()));
                                if(user.getAvatar()!=null){
                                    Glide.with(getContext()).load(user.getAvatar()).into(avatar);
                                }
                                break;
                            }
                            else {

                            }
                        }
                        else {
                            if(emailUser.equals(user.getEmail())) {
                                FirebaseFirestore.getInstance().collection("profiles").
                                        document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        UserProfile userProfile = documentSnapshot.toObject(UserProfile.class);
                                        if(userProfile.getFollowed().contains(idUser)){
                                            setting.setText("Following");
                                        }
                                        else {
                                            setting.setText("Follow");
                                        }
                                    }
                                });
                                txt_followers.setText(String.valueOf(user.getFollower().size()));
                                txt_followings.setText(String.valueOf(user.getFollowed().size()));
                                fullname.setText(user.getFullname().toString());
                                bio.setText(user.getBio().toString());
                                txt_post.setText(String.valueOf(postAdapterProfile.getItemCount()));
                                if(user.getAvatar()!=null){
                                    Glide.with(getContext()).load(user.getAvatar()).into(avatar);
                                }
                                break;
                            }
                        }

                    }


                } else {
                    Toast.makeText(getContext(), "No find", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void unfollow(String idUser){
        List<String> follows = new ArrayList<>();
        List<String> followers = new ArrayList<>();
        CollectionReference db = FirebaseFirestore.getInstance().collection("profiles");
        db.document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                followers.addAll((ArrayList<String>) documentSnapshot.get("follower"));
                followers.remove(firebaseUser.getUid());
                db.document(idUser).update("follower", followers);
                ShowUser(emailUser,idUser);
            }
        });
        db.document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                follows.addAll((ArrayList<String>) documentSnapshot.get("followed"));
                follows.remove(idUser);
                db.document(firebaseUser.getUid()).update("followed", follows);
            }
        });
    }
    public void follow(String idUser){
        List<String> follows = new ArrayList<>();
        List<String> followers = new ArrayList<>();
        CollectionReference db = FirebaseFirestore.getInstance().collection("profiles");
        db.document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                followers.addAll((ArrayList<String>) documentSnapshot.get("follower"));
                followers.add(firebaseUser.getUid());
                db.document(idUser).update("follower", followers);
                ShowUser(emailUser,idUser);
            }
        });
        db.document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                follows.addAll((ArrayList<String>) documentSnapshot.get("followed"));
                follows.add(idUser);
                db.document(firebaseUser.getUid()).update("followed", follows);
            }
        });


    }
}