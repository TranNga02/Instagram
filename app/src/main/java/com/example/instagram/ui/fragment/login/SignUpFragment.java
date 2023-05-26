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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.databinding.FragmentSignUpBinding;
import com.example.instagram.ui.activity.MainActivity;
import com.example.instagram.viewmodel.SignUpViewModel;
import com.google.android.gms.tasks.Task;

public class SignUpFragment extends Fragment {
    private FragmentSignUpBinding binding;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        progressDialog = new ProgressDialog(getContext());
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SignUpViewModel signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Sign up");

                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();
                String fullname = binding.etFullname.getText().toString();
                String username = binding.etUsername.getText().toString();
                String reEnterPass = binding.etReEnterPassword.getText().toString();

                if(email.isEmpty() || password.isEmpty() || fullname.isEmpty() || username.isEmpty() || reEnterPass.isEmpty()){
                    Toast.makeText(getContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
                } else if(!password.equals(reEnterPass)){
                    Toast.makeText(getContext(), "Passwords don't match.", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.show();
                    Task<Boolean> signUpTask = signUpViewModel.signUp(email, password, fullname, username);

                    signUpTask.addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful() && task.getResult()) {
                            Toast.makeText(getContext(), "Sign up successful", Toast.LENGTH_SHORT).show();

                            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_sign_in);
                            navController.navigate(R.id.logInFragment);
                        } else {
                            Toast.makeText(getContext(), "Email already exists. Please choose a different email.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        binding.btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_sign_in);
                navController.navigate(R.id.logInFragment);
            }
        });
    }
}