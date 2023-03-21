package com.example.instagram.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.instagram.data.model.Post;

import java.util.List;

public class PostRepository {
    private MutableLiveData<Post> post; // post details fragment
    private MutableLiveData<List<Post>> posts;
    public PostRepository() {
        post = new MutableLiveData<>();
        posts = new MutableLiveData<>();
    }
    public MutableLiveData<List<Post>> getPosts() {
        post = new MutableLiveData<>();
        return posts;
    }
    public MutableLiveData<Post> getPost() {
        post = new MutableLiveData<>();
        return post;
    }
}
