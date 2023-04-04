package com.example.instagram.repository;

import androidx.annotation.NonNull;

import com.example.instagram.ui.model.PostFeed;
import com.example.instagram.ui.model.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

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

    public void getUserById(String userId,final UserRepository.UserCallback callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("profiles")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            UserProfile user = new UserProfile();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(userId.equals(document.getId())){
                                    user = document.toObject(UserProfile.class);
                                }
                            }
                            callback.onUserByIdLoaded(user);
                        } else {
                            System.out.println("Error getting documents." + task.getException());
                        }
                    }
                });
    }

    public interface UserCallback {
        void onUserByIdLoaded(UserProfile user);
    }
}
