package com.example.instagram.repository;
import androidx.annotation.NonNull;

import com.example.instagram.ui.model.PostFeed;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
                                String postUserId = document.getString("user-id");
                                PostFeed post = document.toObject(PostFeed.class);
                                post.setUserId(postUserId);

                                // Lấy thông tin người dùng ứng với user-id của bài post
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
        DocumentReference postRef = db.collection("posts").document("postId");
    }

    public interface PostCallback {
        void onPostsLoaded(ArrayList<PostFeed> postsList);
    }
}
