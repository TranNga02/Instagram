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
import com.example.instagram.databinding.FragmentFogotPasswordBinding;
import com.example.instagram.databinding.FragmentSignUpBinding;
import com.example.instagram.ui.activity.MainActivity;
import com.example.instagram.viewmodel.ForgetPasswordViewModel;
import com.example.instagram.viewmodel.SignUpViewModel;
import com.google.android.gms.tasks.Task;

public class FogotPasswordFragment extends Fragment {
    private FragmentFogotPasswordBinding binding;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFogotPasswordBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        progressDialog = new ProgressDialog(getContext());
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ForgetPasswordViewModel forgotPasswordViewModel = new ViewModelProvider(this).get(ForgetPasswordViewModel.class);

        binding.btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String email = binding.etEmail.getText().toString();

                Task<Boolean> sendmailTask = forgotPasswordViewModel.forgotPassword(email);

                sendmailTask.addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful() && task.getResult()) {
                        Toast.makeText(getContext(), "Password reset email sent successfully. Please check your mail.", Toast.LENGTH_SHORT).show();
                        NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_sign_in);
                        navController.navigate(R.id.logInFragment);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Forgot password");
                        builder.setMessage("Error sending password reset email: "+ email);
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

        binding.btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_sign_in);
                navController.navigate(R.id.logInFragment);
            }
        });
    }
}