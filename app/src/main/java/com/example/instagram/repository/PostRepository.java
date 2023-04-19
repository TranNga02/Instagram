package com.example.instagram.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.instagram.ui.model.PostFeed;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PostRepository {

    public void getPostOfCurrentUser(final PostCallback callback){
        ArrayList<PostFeed> postArrayList = new ArrayList<>();
//        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                if(userId.equals(document.getString("user-id"))){
//                                String postId = document.getId();
                                PostFeed post = document.toObject(PostFeed.class); // create a new Post instance using the data from the document
                                post.setUserId(document.getString("user-id"));
                                postArrayList.add(post); // add the post to the list
//                                }
                            }
                            callback.onPostsLoaded(postArrayList);
                        } else {
                            System.out.println("Error getting documents." + task.getException());
                        }
                    }
                });
    }

    public interface PostCallback {
        void onPostsLoaded(ArrayList<PostFeed> postsList);
    }
}
