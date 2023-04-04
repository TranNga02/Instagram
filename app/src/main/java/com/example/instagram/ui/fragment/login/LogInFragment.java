package com.example.instagram.ui.fragment.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagram.databinding.FragmentLogInBinding;
import com.example.instagram.ui.activity.MainActivity;
import com.example.instagram.viewmodel.LogInViewModel;
import com.google.android.gms.tasks.Task;

public class LogInFragment extends Fragment {
    private FragmentLogInBinding binding;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogInBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        progressDialog = new ProgressDialog(getContext());
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogInViewModel logInViewModel = new ViewModelProvider(this).get(LogInViewModel.class);

        binding.btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                String email = binding.etUsername.getText().toString();
                String password = binding.etPassword.getText().toString();

                Task<Boolean> signInTask = logInViewModel.logIn(email, password);

                signInTask.addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful() && task.getResult()) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finishAffinity();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Log in");
                        builder.setMessage("Incorrect email or password.");
                        builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.show();
                    }
                });
            }
        });
    }
}