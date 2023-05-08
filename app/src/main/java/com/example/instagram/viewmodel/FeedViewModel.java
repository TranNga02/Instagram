package com.example.instagram.viewmodel;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.instagram.repository.PostRepository;
import com.example.instagram.repository.UserRepository;
import com.example.instagram.ui.model.PostFeed;
import com.example.instagram.ui.model.UserProfile;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FeedViewModel extends ViewModel {
    private FirebaseFirestore firestore;
    private MutableLiveData<ArrayList<PostFeed>> posts;
    private MutableLiveData<ArrayList<PostFeed>> detailPost;
    private PostRepository postRepo;
    private NotificationViewModel notificationViewModel;
    private CollectionReference postCollectionRef;

    public FeedViewModel() {
        postRepo = new PostRepository();
        firestore = FirebaseFirestore.getInstance();
        postCollectionRef = firestore.collection("posts");

        // Đăng ký lắng nghe sự kiện trên collection 'post' trong Firestore
        postCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
                getPostOfCurrentUser();
            }
        });
    }

    public LiveData<ArrayList<PostFeed>> getPosts(){
        if(posts == null){
            posts = new MutableLiveData<>();
            posts.setValue(new ArrayList<>());
        }
        return posts;
    }

    public LiveData<ArrayList<PostFeed>> getDetailPost(){
        if(detailPost == null){
            detailPost = new MutableLiveData<>();
            detailPost.setValue(new ArrayList<>());
        }
        return detailPost;
    }

    public void getPostById(String postId){
        postRepo.getPostById(postId, new PostRepository.PostCallback(){
            @Override
            public void onPostsLoaded(ArrayList<PostFeed> postsList) {
                if (detailPost != null) {
                    detailPost.setValue(postsList);
                }
            }
        });
    }

    public void getPostOfCurrentUser(){
        postRepo.getFullPost(new PostRepository.PostCallback() {
            @Override
            public void onPostsLoaded(ArrayList<PostFeed> postsList) {
                if (posts != null) {
                    posts.setValue(postsList);
                }
            }
        });
    }

    public void likePost(String postId, String userId, String postOwnerId){
        postRepo.updateLikeOfPost(postId, userId);
        notificationViewModel.addNotification("likePost", userId, postId, postOwnerId);
    }

    public void removeLikePost(String postId, String userId){
        postRepo.updateLikeOfPost(postId, userId);
    }
}
