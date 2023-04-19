package com.example.instagram.viewmodel;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.instagram.repository.PostRepository;
import com.example.instagram.repository.UserRepository;
import com.example.instagram.ui.model.PostFeed;
import com.example.instagram.ui.model.UserProfile;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FeedViewModel extends ViewModel {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private MutableLiveData<ArrayList<PostFeed>> posts;
    private PostRepository postRepo;

    public FeedViewModel() {
        postRepo = new PostRepository();
    }

    public LiveData<ArrayList<PostFeed>> getPosts(){
        if(posts == null){
            posts = new MutableLiveData<>();
            posts.setValue(new ArrayList<>());
        }
        return posts;
    }

    public void getPostOfCurrentUser(){
        postRepo.getFullPost(new PostRepository.PostCallback() {
            @Override
            public void onPostsLoaded(ArrayList<PostFeed> postsList) {
                posts.setValue(postsList);
            }
        });
    }

//    public void registerFirestoreListener() {
//        firestore.collection("posts")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) {
//                            return;
//                        }
//                        for (DocumentChange dc : value.getDocumentChanges()) {
//                            DocumentSnapshot doc = dc.getDocument();
//
//                        }
//                    }
//                });
//    }
//
//    @Override
//    protected void onCleared() {
//        super.onCleared();
//        // Hủy đăng ký lắng nghe sự kiện trên Firestore khi ViewModel bị xóa
//        firestore.clearPersistence();
//    }
}
