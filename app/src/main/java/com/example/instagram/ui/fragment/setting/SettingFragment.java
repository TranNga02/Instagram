package com.example.instagram.ui.fragment.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences preferences = getContext().getSharedPreferences("PREPS", Context.MODE_PRIVATE);
        profileId = preferences.getString("profileID","none");
        avatar = view.findViewById(R.id.frgSettingsImgProfilePhoto);
        email = view.findViewById(R.id.frgSettingsTxtUserEmail);
        name = view.findViewById(R.id.frgSettingsTxtUserFullName);
        pw = view.findViewById(R.id.frgSettingsTxtUserPassword);
        bio = view.findViewById(R.id.frgSettingsTxtUserBio);

        btnSave = view.findViewById(R.id.frgSettingsBtnSave);
        btnSelectImg = view.findViewById(R.id.frgSettingsBtnSelectPhoto);
        btnUpdateImg = view.findViewById(R.id.frgSettingsBtnUpdatePhoto);
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
    public void show(){

    }
}