package com.example.instagram.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.instagram.repository.PostRepository;
import com.example.instagram.repository.UserRepository;
import com.example.instagram.ui.model.PostFeed;
import com.example.instagram.ui.model.UserProfile;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class FeedViewModel extends ViewModel {

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
}
