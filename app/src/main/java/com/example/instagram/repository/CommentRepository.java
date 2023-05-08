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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class CommentRepository {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getCommentsByPostId(String postId, final CommentCallback callback){
        ArrayList<Comment> commentArrayList = new ArrayList<>();

        Query commentsQuery = db.collection("comments")
                .whereEqualTo("postId", postId)
                .orderBy("time", Query.Direction.ASCENDING);

        commentsQuery.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot docSnapshot : queryDocumentSnapshots) {
                            Comment comment = docSnapshot.toObject(Comment.class);
                            String userId = docSnapshot.getString("userId");
                            comment.setId(docSnapshot.getId());

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
                                        // Sắp xếp commentArrayList theo thời gian (time)
                                        Collections.sort(commentArrayList, new Comparator<Comment>() {
                                            @Override
                                            public int compare(Comment comment1, Comment comment2) {
                                                return comment2.getTime().compareTo(comment1.getTime());
                                            }
                                        });
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
        void onCommentsLoaded(ArrayList<Comment> commentsList);
    }

    public void addComment(String content, String postId, String userId){
        DocumentReference commentRef = db.collection("comments").document();

        Map<String, Object> commentData = new HashMap<>();
        commentData.put("content", content);
        commentData.put("likes", Arrays.asList());
        commentData.put("postId", postId);
        commentData.put("time", new Date());
        commentData.put("userId", userId);

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

    public void updateLikeOfComment(String commentId, String userId){
        DocumentReference commentRef = db.collection("comments").document(commentId);

        commentRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Get the current likes array
                Object likesObject = documentSnapshot.get("likes");
                if (likesObject instanceof ArrayList) {
                    ArrayList<String> likesArray = (ArrayList<String>) likesObject;
                    if (likesArray.contains(userId)) {
                        // If userId is already in the likes array, remove it
                        commentRef.update("likes", FieldValue.arrayRemove(userId))
                                .addOnSuccessListener(aVoid -> {
                                    // If userId was removed successfully
                                    System.out.println("Successfully removed userId from likes array.");
                                })
                                .addOnFailureListener(e -> {
                                    // If there was an error removing userId from likes array, handle it here
                                    System.err.println("Error removing userId from likes array: " + e.getMessage());
                                });
                    } else {
                        // If userId is not in the likes array, add it
                        commentRef.update("likes", FieldValue.arrayUnion(userId))
                                .addOnSuccessListener(aVoid -> {
                                    // If userId was added successfully
                                    System.out.println("Successfully added userId to likes array.");
                                })
                                .addOnFailureListener(e -> {
                                    // If there was an error adding userId to likes array, handle it here
                                    System.err.println("Error adding userId to likes array: " + e.getMessage());
                                });
                    }
                }
            }
        }).addOnFailureListener(e -> {
            // If there was an error getting the document, handle it here
            System.err.println("Error getting post document: " + e.getMessage());
        });
    }
}
