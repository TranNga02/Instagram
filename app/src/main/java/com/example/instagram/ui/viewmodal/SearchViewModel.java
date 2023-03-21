package com.example.instagram.ui.viewmodal;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.instagram.data.model.User;
import com.example.instagram.data.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SearchViewModel extends ViewModel {
    private UserRepository uRepo;
    private MutableLiveData<List<User>> users;

    @Inject
    public SearchViewModel(UserRepository uRepo) {
        this.uRepo = uRepo;

        this.users = uRepo.getUsers();
    }

    public MutableLiveData<List<User>> getUsers() {
        return users;
    }

//    public void filterUsersByName(String user_name) {
//        uRepo.filterUsersByName(user_name);
//    }
}
