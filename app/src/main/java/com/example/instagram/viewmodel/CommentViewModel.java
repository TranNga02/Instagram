package com.example.instagram.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.instagram.repository.CommentRepository;
import com.example.instagram.repository.PostRepository;
import com.example.instagram.repository.UserRepository;
import com.example.instagram.ui.model.Comment;
import com.example.instagram.ui.model.PostFeed;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CommentViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Comment>> comments;
    private MutableLiveData<String> userAvatar;
    private CommentRepository commentRepo;
    private UserRepository userRepo;
    private CollectionReference commentCollectionRef;
    private FirebaseFirestore firestore;
    private String postId;

    public CommentViewModel() {
        commentRepo = new CommentRepository();
        userRepo = new UserRepository();
        firestore = FirebaseFirestore.getInstance();
        commentCollectionRef = firestore.collection("comments");

        // Đăng ký lắng nghe sự kiện trên collection 'post' trong Firestore
        commentCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
                getCommentsOfPost(postId);
            }
        });
    }

    public LiveData<ArrayList<Comment>> getComments(){
        if(comments == null){
            comments = new MutableLiveData<>();
            comments.setValue(new ArrayList<>());
        }
        return comments;
    }

    public LiveData<String> getUserAvatar(){
        if(userAvatar == null){
            userAvatar = new MutableLiveData<>();
            userAvatar.setValue(new String());
        }
        return userAvatar;
    }

    public void getCommentsOfPost(String postId){
        this.postId = postId;
        commentRepo.getCommentsByPostId(postId, new CommentRepository.CommentCallback() {
            @Override
            public void onCommentsLoaded(ArrayList<Comment> commentArrayList) {
                comments.setValue(commentArrayList);
            }
        });
    }

    public void getUserAvatarById(String userId){
        userRepo.getUserAvatar(userId, new UserRepository.UserCallback() {
            @Override
            public void onAvatarLoaded(String avatar) {
                userAvatar.setValue(avatar);
            }
        });
    }

    public void addComment(String content, String postId, String userId){
        commentRepo.addComment(content, postId, userId);
    }

    public void updateLikeOfComment(String commentId, String userId){
        commentRepo.updateLikeOfComment(commentId, userId);
    }
}
