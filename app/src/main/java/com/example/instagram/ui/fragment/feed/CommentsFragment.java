package com.example.instagram.ui.fragment.feed;

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

import com.example.instagram.databinding.FragmentCommentsBinding;
import com.example.instagram.ui.adapter.CommentsAdapter;
import com.example.instagram.ui.model.Comment;
import com.example.instagram.viewmodel.CommentViewModel;

import java.util.ArrayList;

public class CommentsFragment extends Fragment {
    private FragmentCommentsBinding binding;
    ArrayList<Comment> commentArrayList;
    CommentsAdapter commentsAdapter;
    String postId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCommentsBinding.inflate(inflater, container, false);

        postId = getArguments().getString("post-id");

        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CommentViewModel commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);

        commentArrayList = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(getContext(), commentArrayList);

        binding.rvFragmentComments.setAdapter(commentsAdapter);
        binding.rvFragmentComments.setLayoutManager(new LinearLayoutManager(getContext()));

        commentViewModel.getComments().observe(getViewLifecycleOwner(), new Observer<ArrayList<Comment>>() {
            @Override
            public void onChanged(ArrayList<Comment> comments) {
                commentArrayList.clear();
                commentArrayList.addAll(comments);
                commentsAdapter.notifyDataSetChanged();
            }
        });

        commentViewModel.getCommentsOfPost(postId);
    }
}