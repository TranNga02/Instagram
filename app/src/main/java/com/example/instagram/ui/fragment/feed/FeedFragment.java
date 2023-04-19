package com.example.instagram.ui.fragment.feed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagram.R;
import com.example.instagram.databinding.FragmentFeedBinding;
import com.example.instagram.repository.PostRepository;
import com.example.instagram.ui.adapter.PostAdapterFeed;
import com.example.instagram.ui.model.PostFeed;
import com.example.instagram.viewmodel.FeedViewModel;
import com.example.instagram.viewmodel.LogInViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {
    private FragmentFeedBinding binding;
    ArrayList<PostFeed> postArrayList;
    PostAdapterFeed postAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFeedBinding.inflate(inflater, container, false);
        binding.btnMessage.setOnClickListener(new View.OnClickListener() {
                    //@Override
                    public void onClick(View v) {
                        NavController navController = Navigation.findNavController(getActivity(), R.id.fragmentContainerView);
                        NavOptions navOptions = new NavOptions.Builder()
                                .setPopUpTo(R.id.feedFragment, true)
                                .build();
                        navController.navigate(R.id.messageFragment, null, navOptions);
                    }
                });
        View rootView = binding.getRoot();
        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FeedViewModel feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
//        // Gọi phương thức registerFirestoreListener để đăng ký lắng nghe sự kiện trên Firestore
//        feedViewModel.registerFirestoreListener();

        postArrayList = new ArrayList<>();
        postAdapter = new PostAdapterFeed(getContext(), postArrayList);

        binding.rvFragmentFeed.setAdapter(postAdapter);
        binding.rvFragmentFeed.setLayoutManager(new LinearLayoutManager(getContext()));

        feedViewModel.getPosts().observe(getViewLifecycleOwner(), new Observer<ArrayList<PostFeed>>() {
            @Override
            public void onChanged(ArrayList<PostFeed> postFeeds) {
                postArrayList.clear();
                postArrayList.addAll(postFeeds);
                postAdapter.notifyDataSetChanged();
            }
        });

        feedViewModel.getPostOfCurrentUser();
    }
}