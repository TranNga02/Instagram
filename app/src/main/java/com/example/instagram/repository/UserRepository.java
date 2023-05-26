package com.example.instagram.repository;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

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

    public void getUserIdByPostId(String postId, final UserCallback callback){
        DocumentReference postDocRef = db.collection("posts").document(postId);
        postDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> postTask) {
                if (postTask.isSuccessful()) {
                    DocumentSnapshot postSnapshot = postTask.getResult();
                    if (postSnapshot.exists()) {
                        callback.onUserIdLoaded(postSnapshot.getString("userId"));
                    } else {
                        System.out.println("Không tìm thấy userId ứng với postId: " + postId);
                    }
                } else {
                    System.out.println("Lỗi khi lấy dữ liệu post: " + postTask.getException());
                }
            }
        });
    }

    public Task<Boolean> signUp(String email, String password, String fullname, String username){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        TaskCompletionSource<Boolean> signUpResult = new TaskCompletionSource<>();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("DEBUG", "createUserWithEmail:success");
                        System.out.println("//////////////////////////Sign up success");
                        String uid = mAuth.getCurrentUser().getUid();

                        addNewProfile(uid, email, fullname, username);
                        signUpResult.setResult(true);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("DEBUG", "createUserWithEmail:failure", task.getException());
                        System.out.println("////////////////////////Sign up fail");
                        signUpResult.setResult(false);
                    }
                });
        return signUpResult.getTask();

    }

    public void addNewProfile(String id, String email, String fullname, String username) {
        DocumentReference profileRef = db.collection("profiles").document(id);

        Map<String, Object> profileData = new HashMap<>();
        profileData.put("avatar", "");
        profileData.put("bio", "");
        profileData.put("birthday", "");
        profileData.put("email", email);
        profileData.put("followed", Arrays.asList());
        profileData.put("follower", Arrays.asList());
        profileData.put("likes", Arrays.asList());
        profileData.put("fullname", fullname);
        profileData.put("username", username);
        profileData.put("gender", "");
        profileData.put("linkFB", "");

        profileRef.set(profileData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document added successfully
                        Log.d("TAG", "Profile document added with ID: " + id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add document
                        Log.e("TAG", "Error adding profile document", e);
                    }
                });
    }

    public Task<Boolean> sendMail(String emailAddress){
        TaskCompletionSource<Boolean> sendMailResult = new TaskCompletionSource<>();
        FirebaseAuth.getInstance().sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sendMailResult.setResult(true);
                    } else {
                        sendMailResult.setResult(false);
                    }
                });
        return sendMailResult.getTask();
    }

    public interface UserCallback {
        void onAvatarLoaded(String avatar);
        void onUsernameLoaded(String username);
        void onUserIdLoaded(String userId);
    }
}
