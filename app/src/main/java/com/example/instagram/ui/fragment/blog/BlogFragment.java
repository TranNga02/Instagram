package com.example.instagram.ui.fragment.blog;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.ui.fragment.setting.SettingFragment;
import com.example.instagram.ui.model.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;

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

                }
            }
        });

        ShowUser();
        return view;

    }

    ShapeableImageView avatar;
    TextView  txt_post, txt_followers, txt_followings, fullname, bio;
    Button setting;

    FirebaseUser firebaseUser;
    String profileId;

    public void ShowUser(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("profiles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        UserProfile user = document.toObject(UserProfile.class); // create a new Post instance using the data from the document
                        if(firebaseUser.getEmail().equals(user.getEmail())) {
                            txt_followers.setText(String.valueOf(user.getFollower().size()));
                            txt_followings.setText(String.valueOf(user.getFollowed().size()));
                            fullname.setText(user.getFullname().toString());
                            bio.setText(user.getBio().toString());
                            Glide.with(getContext()).load(user.getAvatar()).into(avatar);
                            break;
                        }
                        else {
                            Toast.makeText(getContext(), "No find", Toast.LENGTH_SHORT).show();
                        }
                    }


                } else {
                    Toast.makeText(getContext(), "No find", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}