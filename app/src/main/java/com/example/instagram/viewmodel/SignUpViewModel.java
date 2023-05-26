package com.example.instagram.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.instagram.repository.UserRepository;
import com.google.android.gms.tasks.Task;

public class SignUpViewModel extends ViewModel {
    private MutableLiveData<String> email, fullname, username, password, reEnterPass;
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

    public LiveData<String> getFullname(){
        if(fullname == null){
            fullname = new MutableLiveData<>();
            fullname.setValue("");
        }
        return fullname;
    }

    public LiveData<String> getUsername(){
        if(username == null){
            username = new MutableLiveData<>();
            username.setValue("");
        }
        return username;
    }

    public LiveData<String> getReEnterPass(){
        if(reEnterPass == null){
            reEnterPass = new MutableLiveData<>();
            reEnterPass.setValue("");
        }
        return reEnterPass;
    }

    public Task<Boolean> signUp(String email, String password, String fullname, String username){
        userRepo = new UserRepository();
        return userRepo.signUp(email, password, fullname, username);
    }
}
