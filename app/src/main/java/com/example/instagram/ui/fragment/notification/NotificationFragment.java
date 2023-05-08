package com.example.instagram.ui.fragment.notification;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagram.R;
import com.example.instagram.databinding.FragmentCommentsBinding;
import com.example.instagram.databinding.FragmentNotificationBinding;
import com.example.instagram.ui.adapter.NotificationsAdapter;
import com.example.instagram.ui.model.Notification;
import com.example.instagram.viewmodel.CommentViewModel;
import com.example.instagram.viewmodel.NotificationViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {
    private FragmentNotificationBinding binding;
    ArrayList<Notification> notificationsArrayList;
    NotificationsAdapter notificationsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NotificationViewModel viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        notificationsArrayList = new ArrayList<>();
        notificationsAdapter = new NotificationsAdapter(getContext(), notificationsArrayList);

        binding.rvFragmentNotifications.setAdapter(notificationsAdapter);
        binding.rvFragmentNotifications.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.getNotifications().observe(getViewLifecycleOwner(), new Observer<ArrayList<Notification>>() {
            @Override
            public void onChanged(ArrayList<Notification> notifications) {
                notificationsArrayList.clear();
                notificationsArrayList.addAll(notifications);
                notificationsAdapter.notifyDataSetChanged();
            }
        });

        viewModel.getAllNotifications();
    }
}