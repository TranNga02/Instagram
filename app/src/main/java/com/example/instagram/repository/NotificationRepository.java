package com.example.instagram.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.instagram.ui.model.Comment;
import com.example.instagram.ui.model.Notification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public void loadNotification(final NotificationCallBack callBack){
        ArrayList<Notification> notificationArrayList = new ArrayList<>();
        String userId = FirebaseAuth.getInstance().getUid();

        Query notificationsQuery = db.collection("notifications")
                .whereEqualTo("ownUserId", userId)
                .orderBy("time", Query.Direction.ASCENDING);

        notificationsQuery.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Notification notification = documentSnapshot.toObject(Notification.class);
                            notification.setId(documentSnapshot.getId());

                            DocumentReference userDocRef = db.collection("profiles").document(notification.getMakeUserId());
                            userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> userTask) {
                                    if (userTask.isSuccessful()) {
                                        DocumentSnapshot userSnapshot = userTask.getResult();
                                        if (userSnapshot.exists()) {
                                            notification.setAvatar(userSnapshot.getString("avatar"));
                                        } else {
                                            System.out.println("Không tìm thấy avatar người dùng ứng với userId: " + userId);
                                        }
                                    } else {
                                        System.out.println("Lỗi khi lấy dữ liệu người dùng: " + userTask.getException());
                                    }

                                    // Sau khi hoàn thành việc lấy thông tin người dùng, cập nhật dữ liệu vào postArrayList
                                    notificationArrayList.add(notification);

                                    // Kiểm tra xem đã lấy thông tin cho hết các bài post chưa
                                    if (notificationArrayList.size() == queryDocumentSnapshots.size()) {
                                        // Nếu đã lấy thông tin cho hết các bài post, gọi callback để thông báo hoàn thành
                                        // Sắp xếp commentArrayList theo thời gian (time)
                                        Collections.sort(notificationArrayList, new Comparator<Notification>() {
                                            @Override
                                            public int compare(Notification no1, Notification no2) {
                                                return no2.getTime().compareTo(no1.getTime());
                                            }
                                        });
                                        callBack.onNotificationsLoaded(notificationArrayList);
                                    }
                                }
                            });
                        }
                    }
                });
    }

    public interface NotificationCallBack{
        void onNotificationsLoaded(ArrayList<Notification> notifications);
    }
}
