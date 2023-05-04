package com.example.instagram.ui.fragment.feed;

import android.content.Context;
import android.net.Uri;
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
import android.view.inputmethod.InputMethodManager;

import com.bumptech.glide.Glide;
import com.example.instagram.databinding.FragmentCommentsBinding;
import com.example.instagram.ui.adapter.CommentsAdapter;
import com.example.instagram.ui.model.Comment;
import com.example.instagram.viewmodel.CommentViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CommentsFragment extends Fragment {
    private FragmentCommentsBinding binding;
    ArrayList<Comment> commentArrayList;
    CommentsAdapter commentsAdapter;
    String postId, userId, postOwnerId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCommentsBinding.inflate(inflater, container, false);

        postId = getArguments().getString("post-id");
        postOwnerId = getArguments().getString("post-owner-id");
        userId = FirebaseAuth.getInstance().getUid();

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
        commentViewModel.getUserAvatar().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String avatar) {
                if (avatar != null) {
                    Uri avatarUri = Uri.parse(avatar);
                    Glide.with(getContext())
                            .load(avatarUri)
                            .into(binding.ivUserAvatar);
                }
            }
        });
        commentViewModel.getCommentsOfPost(postId);
        commentViewModel.getUserAvatarById(userId);

        binding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentViewModel.addComment(binding.etComment.getText().toString(), postId, userId, postOwnerId);
                binding.etComment.clearFocus();
                binding.etComment.setText("");
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.etComment.getWindowToken(), 0);
            }
        });
    }
}