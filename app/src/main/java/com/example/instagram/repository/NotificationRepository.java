package com.example.instagram.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NotificationRepository {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addNotification(String content, String makeUserId, String postId,String ownUserId){
        DocumentReference notificationRef = db.collection("notifications").document();

        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("content", content);
        notificationData.put("ownUserId", ownUserId);
        notificationData.put("postId", postId);
        notificationData.put("time", new Date());
        notificationData.put("makeUserId", makeUserId);

        notificationRef.set(notificationData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document added successfully
                        Log.d("TAG", "Notification document added with ID: " + notificationRef.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add document
                        Log.e("TAG", "Error adding notification document", e);
                    }
                });
    }
}
