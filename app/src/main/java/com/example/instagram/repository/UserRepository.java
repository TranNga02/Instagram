package com.example.instagram.repository;

import androidx.annotation.NonNull;

import com.example.instagram.ui.model.Comment;
import com.example.instagram.ui.model.PostFeed;
import com.example.instagram.ui.model.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

    public void getUserAvatar(String userId,final UserCallback callback){
        DocumentReference userDocRef = db.collection("profiles").document(userId);
        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> userTask) {
                if (userTask.isSuccessful()) {
                    DocumentSnapshot userSnapshot = userTask.getResult();
                    if (userSnapshot.exists()) {
                        String avatar = userSnapshot.getString("avatar");
                        callback.onAvatarLoaded(avatar);
                    } else {
                        System.out.println("Không tìm thấy dữ liệu người dùng ứng với userId: " + userId);
                    }
                } else {
                    System.out.println("Lỗi khi lấy dữ liệu người dùng: " + userTask.getException());
                }
            }
        });
    }

    public void getCurrentUsername(final UserCallback callback){
        DocumentReference userDocRef = db.collection("profiles").document(FirebaseAuth.getInstance().getUid());
        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> userTask) {
                if (userTask.isSuccessful()) {
                    DocumentSnapshot userSnapshot = userTask.getResult();
                    if (userSnapshot.exists()) {
                        String username = userSnapshot.getString("username");
                        callback.onUsernameLoaded(username);
                    } else {
                        System.out.println("Không tìm username ứng với userId: " + FirebaseAuth.getInstance().getUid());
                    }
                } else {
                    System.out.println("Lỗi khi lấy dữ liệu người dùng: " + userTask.getException());
                }
            }
        });
    }

    public interface UserCallback {
        void onAvatarLoaded(String avatar);
        void onUsernameLoaded(String username);
    }
}
