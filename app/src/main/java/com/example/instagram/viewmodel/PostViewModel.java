package com.example.instagram.viewmodel;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.example.instagram.repository.NotificationRepository;
import com.example.instagram.repository.PostRepository;
import com.example.instagram.repository.UserRepository;
import com.example.instagram.ui.model.Notification;
import com.example.instagram.ui.model.PostFeed;

import java.util.ArrayList;

public class PostViewModel extends ViewModel {
    private PostRepository postRepo;
    private NotificationRepository notificationRepo;
    private UserRepository userRepo;

    public PostViewModel() {
        postRepo = new PostRepository();
        notificationRepo = new NotificationRepository();
        userRepo = new UserRepository();
    }

    public void addPost(Context context, String content, Uri imageUri){
        postRepo.addNewPost(context, content, imageUri);
    }
}
