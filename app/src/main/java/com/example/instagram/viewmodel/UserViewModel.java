package com.example.instagram.viewmodel;

import com.example.instagram.repository.UserRepository;
import com.example.instagram.ui.model.UserProfile;

public class UserViewModel {
    private UserRepository userRepo;

    public UserProfile getUserById(String userId){
        final UserProfile[] userProfile = {new UserProfile()};
        userRepo.getUserById(userId, new UserRepository.UserCallback() {
            @Override
            public void onUserByIdLoaded(UserProfile user) {
                userProfile[0] = user;
            }
        });
        return userProfile[0];
    }
}
