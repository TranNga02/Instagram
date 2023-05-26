package com.example.instagram.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.instagram.repository.UserRepository;
import com.google.android.gms.tasks.Task;

public class ForgetPasswordViewModel extends ViewModel {
    private MutableLiveData<String> email;

    public LiveData<String> getEmail(){
        if(email == null){
            email = new MutableLiveData<>();
            email.setValue("");
        }
        return email;
    }

    public Task<Boolean> forgotPassword(String email){
        return new UserRepository().sendMail(email);
    }
}
