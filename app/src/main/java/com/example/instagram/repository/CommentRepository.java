package com.example.instagram.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.instagram.ui.model.Comment;
import com.example.instagram.ui.model.PostFeed;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentRepository {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getCommentsByPostId(String postId, final CommentCallback callback){
        ArrayList<Comment> commentArrayList = new ArrayList<>();
        Query commentsQuery = db.collection("comments").whereEqualTo("post-id", postId);

        commentsQuery.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot docSnapshot : queryDocumentSnapshots) {
                            Comment comment = docSnapshot.toObject(Comment.class);
                            String userId = docSnapshot.getString("user-id");
                            comment.setId(docSnapshot.getId());
                            comment.setUserId(userId);
                            comment.setPostId(docSnapshot.getString("post-id"));

                            // Lấy thông tin người dùng ứng với user-id của bài post
                            DocumentReference userDocRef = db.collection("profiles").document(userId);
                            userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> userTask) {
                                    if (userTask.isSuccessful()) {
                                        DocumentSnapshot userSnapshot = userTask.getResult();
                                        if (userSnapshot.exists()) {
                                            String username = userSnapshot.getString("username");
                                            String avatar = userSnapshot.getString("avatar");
                                            comment.setUsername(username);
                                            comment.setAvatar(avatar);
                                        } else {
                                            System.out.println("Không tìm thấy dữ liệu người dùng ứng với userId: " + userId);
                                        }
                                    } else {
                                        System.out.println("Lỗi khi lấy dữ liệu người dùng: " + userTask.getException());
                                    }

                                    // Sau khi hoàn thành việc lấy thông tin người dùng, cập nhật dữ liệu vào postArrayList
                                    commentArrayList.add(comment);

                                    // Kiểm tra xem đã lấy thông tin cho hết các bài post chưa
                                    if (commentArrayList.size() == queryDocumentSnapshots.size()) {
                                        // Nếu đã lấy thông tin cho hết các bài post, gọi callback để thông báo hoàn thành
                                        callback.onCommentsLoaded(commentArrayList);
                                    }
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "Fail to load comments.", e);
                    }
                });
    }

    public interface CommentCallback {
        void onCommentsLoaded(ArrayList<Comment> postsList);
    }

    public void AddComment(String content, String postId, String userId, String time){
        DocumentReference commentRef = db.collection("comments").document();

        Map<String, Object> commentData = new HashMap<>();
        commentData.put("content", content);
        commentData.put("likes", Arrays.asList());
        commentData.put("post-id", postId);
        commentData.put("time", time);
        commentData.put("user-id", userId);

        commentRef.set(commentData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document added successfully
                        Log.d("TAG", "Comment document added with ID: " + commentRef.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add document
                        Log.e("TAG", "Error adding comment document", e);
                    }
                });
    }
}
