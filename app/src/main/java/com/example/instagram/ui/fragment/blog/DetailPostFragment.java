package com.example.instagram.ui.fragment.blog;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.databinding.FragmentDetailPostBinding;
import com.example.instagram.databinding.FragmentFeedBinding;
import com.example.instagram.ui.adapter.PostAdapterFeed;
import com.example.instagram.ui.model.PostFeed;
import com.example.instagram.ui.model.UserProfile;
import com.example.instagram.viewmodel.FeedViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DetailPostFragment extends Fragment {
    private FragmentDetailPostBinding binding;
    ArrayList<PostFeed> postArrayList;
    PostAdapterFeed postAdapter;
    PostFeed post;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailPostBinding.inflate(inflater, container, false);
        postArrayList = new ArrayList<>();
        postAdapter = new PostAdapterFeed(getContext(), postArrayList);
        binding.rvFragmentDetailPost.setAdapter(postAdapter);
        binding.rvFragmentDetailPost.setLayoutManager(new LinearLayoutManager(getContext()));
        View rootView = binding.getRoot();
        Bundle bundle1 = getArguments();
        if (bundle1 != null) {
            String idPost = bundle1.getString("idPost");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("posts").document(idPost).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        postArrayList.add(documentSnapshot.toObject(PostFeed.class));
                        postAdapter.notifyDataSetChanged();
                        Log.i("TAG", "find");

                    } else {
                        // Tài liệu không tồn tại
                        Log.e("TAG", "No such document");
                    }
                }
            });
        }
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.fragmentContainerView);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.detailPostFragment, true)
                        .build();
                navController.navigate(R.id.blogFragment, null, navOptions);
            }
        });
        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        FeedViewModel feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
//
//        postArrayList = new ArrayList<>();
//        postAdapter = new PostAdapterFeed(getContext(), postArrayList);
//
//        binding.rvFragmentDetailPost.setAdapter(postAdapter);
//        binding.rvFragmentDetailPost.setLayoutManager(new LinearLayoutManager(getContext()));
//        postArrayList.add(post);
//        feedViewModel.getPosts().observe(getViewLifecycleOwner(), new Observer<ArrayList<PostFeed>>() {
//            @Override
//            public void onChanged(ArrayList<PostFeed> postFeeds) {
//                postArrayList.clear();
//                postArrayList.addAll(postFeeds);
//                postAdapter.notifyDataSetChanged();
//            }
//        });
//
//        feedViewModel.getPostOfCurrentUser();
    }
}