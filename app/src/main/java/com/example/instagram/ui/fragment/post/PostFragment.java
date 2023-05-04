package com.example.instagram.ui.fragment.post;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.databinding.FragmentLogInBinding;
import com.example.instagram.databinding.FragmentPostBinding;
import com.example.instagram.viewmodel.CommentViewModel;
import com.example.instagram.viewmodel.PostViewModel;

public class PostFragment extends Fragment {
    private FragmentPostBinding binding;
    private Uri imageUri;
    public static final int PICK_VIEW = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PostViewModel postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        binding.btnPost.setEnabled(false);
        binding.etContent.setText("");

        binding.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_VIEW);
            }
        });

        binding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri != null){
                    postViewModel.addPost(getContext(), binding.etContent.getText().toString(), imageUri);
                    NavController navController = Navigation.findNavController(getActivity(),R.id.fragmentContainerView);
                    navController.navigate(R.id.feedFragment);
                }else{
                    Toast.makeText(getContext(), "Please choose an image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_VIEW || resultCode == RESULT_OK
                || data!=null || data.getData()!=null){
            imageUri = data.getData();
            Uri imageUri = data.getData();
            Glide.with(getContext())
                    .load(imageUri)
                    .into(binding.ivImage);
            binding.btnPost.setEnabled(true);
        }
    }
}