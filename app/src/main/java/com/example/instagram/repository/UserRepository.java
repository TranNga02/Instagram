package com.example.instagram.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserRepository {



    public Task<Boolean> logIn(String email, String password){
        TaskCompletionSource<Boolean> logInResult = new TaskCompletionSource<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            logInResult.setResult(true);
                        } else {
                            logInResult.setResult(false);

                        }
                    }
                });

        return logInResult.getTask();
    }
}
