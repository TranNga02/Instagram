package com.example.instagram.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.instagram.repository.CommentRepository;
import com.example.instagram.repository.PostRepository;
import com.example.instagram.ui.model.Comment;
import com.example.instagram.ui.model.PostFeed;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CommentViewModel extends ViewModel {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private MutableLiveData<ArrayList<Comment>> comments;
    private CommentRepository commentRepo;

    public CommentViewModel() {
        commentRepo = new CommentRepository();
    }

    public LiveData<ArrayList<Comment>> getComments(){
        if(comments == null){
            comments = new MutableLiveData<>();
            comments.setValue(new ArrayList<>());
        }
        return comments;
    }

    public void getCommentsOfPost(String postId){
        System.out.println("++++++++++++++++++++++++++++++"+postId);
        commentRepo.getCommentsByPostId(postId, new CommentRepository.CommentCallback() {
            @Override
            public void onCommentsLoaded(ArrayList<Comment> commentArrayList) {
                comments.setValue(commentArrayList);
            }
        });
    }
}
