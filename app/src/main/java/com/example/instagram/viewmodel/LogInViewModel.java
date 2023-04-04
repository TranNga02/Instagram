package com.example.instagram.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.instagram.repository.UserRepository;
import com.google.android.gms.tasks.Task;

public class LogInViewModel extends ViewModel {
    private MutableLiveData<String> email, password;
    private UserRepository userRepo;


    public LiveData<String> getEmail(){
        if(email == null){
            email = new MutableLiveData<>();
            email.setValue("");
        }
        return email;
    }

    public LiveData<String> getPassword(){
        if(password == null){
            password = new MutableLiveData<>();
            password.setValue("");
        }
        return password;
    }

    public Task<Boolean> logIn(String email, String password){
        userRepo = new UserRepository();
        return userRepo.logIn(email, password);
    }
}
