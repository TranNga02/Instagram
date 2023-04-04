package com.example.instagram.ui.fragment.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagram.R;
import com.example.instagram.ui.activity.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextFragment();
            }
        }, 1500);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    private void nextFragment() {
//        NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_sign_in);
//        navController.navigate(R.id.logInFragment);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_sign_in);
            navController.navigate(R.id.logInFragment);
        }
        else{
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finishAffinity();
        }
    }
}