package com.example.instagram.ui.fragment.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.ui.model.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SettingFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences preferences = getContext().getSharedPreferences("PREPS", Context.MODE_PRIVATE);
        profileId = preferences.getString("profileID","none");
        avatar = view.findViewById(R.id.frgSettingsImgProfilePhoto);
//        email = view.findViewById(R.id.frgSettingsTxtUserEmail);
        name = view.findViewById(R.id.frgSettingsTxtUserFullName);
//        pw = view.findViewById(R.id.frgSettingsTxtUserPassword);
        bio = view.findViewById(R.id.frgSettingsTxtUserBio);

        btnSave = view.findViewById(R.id.frgSettingsBtnSave);
//        btnSelectImg = view.findViewById(R.id.frgSettingsBtnSelectPhoto);
//        btnUpdateImg = view.findViewById(R.id.frgSettingsBtnUpdatePhoto);
        ShowUser();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnUpdateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    public SettingFragment(){

    }
    ShapeableImageView avatar;
    TextView rmPhoto;
    TextInputEditText email, name, pw, bio;
    Button btnSelectImg, btnUpdateImg, btnSave;

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
                        if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(user.getEmail())) {
                            email.setText("user.getEmail()");
                            name.setText(user.getUsername());
                            bio.setText(user.getBio());
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