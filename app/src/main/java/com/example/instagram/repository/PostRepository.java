package com.example.instagram.repository;
import androidx.annotation.NonNull;

import com.example.instagram.ui.model.PostFeed;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PostRepository {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getFullPost(final PostCallback callback) {
        ArrayList<PostFeed> postArrayList = new ArrayList<>();
        String userId = FirebaseAuth.getInstance().getUid();

        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PostFeed post = document.toObject(PostFeed.class);
                                String postUserId = document.getString("user-id");
                                post.setUserId(postUserId);
                                post.setId(document.getId());

                                // Lấy thông tin người dùng ứng với user-id của comment
                                DocumentReference userDocRef = db.collection("profiles").document(postUserId);
                                userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> userTask) {
                                        if (userTask.isSuccessful()) {
                                            DocumentSnapshot userSnapshot = userTask.getResult();
                                            if (userSnapshot.exists()) {
                                                String username = userSnapshot.getString("username");
                                                String avatar = userSnapshot.getString("avatar");
                                                post.setUsername(username);
                                                post.setAvatar(avatar);
                                            } else {
                                                System.out.println("Không tìm thấy dữ liệu người dùng ứng với userId: " + postUserId);
                                            }
                                        } else {
                                            System.out.println("Lỗi khi lấy dữ liệu người dùng: " + userTask.getException());
                                        }

                                        // Sau khi hoàn thành việc lấy thông tin người dùng, cập nhật dữ liệu vào postArrayList
                                        postArrayList.add(post);

                                        // Kiểm tra xem đã lấy thông tin cho hết các bài post chưa
                                        if (postArrayList.size() == task.getResult().size()) {
                                            // Nếu đã lấy thông tin cho hết các bài post, gọi callback để thông báo hoàn thành
                                            callback.onPostsLoaded(postArrayList);
                                        }
                                    }
                                });
                            }
//                        } else {
//                            System.out.println("Error getting documents." + task.getException());
//                        }}
                        }
                    }
                });
    }

    public void updateLikeOfPost(String postId, String userId){
        DocumentReference postRef = db.collection("posts").document(postId);

        postRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Get the current likes array
                Object likesObject = documentSnapshot.get("likes");
                if (likesObject instanceof ArrayList) {
                    ArrayList<String> likesArray = (ArrayList<String>) likesObject;
                    if (likesArray.contains(userId)) {
                        // If userId is already in the likes array, remove it
                        postRef.update("likes", FieldValue.arrayRemove(userId))
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
                        postRef.update("likes", FieldValue.arrayUnion(userId))
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

    public interface PostCallback {
        void onPostsLoaded(ArrayList<PostFeed> postsList);
    }
}
