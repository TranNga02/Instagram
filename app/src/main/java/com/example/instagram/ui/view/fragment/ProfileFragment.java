package com.example.instagram.ui.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.data.model.Post;
import com.example.instagram.databinding.FragmentProfileBinding;
import com.example.instagram.ui.view.adapter.PostAdapterProfile;
import com.example.instagram.ui.viewmodal.ProfileViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;

    private ProfileViewModel viewModel;

    private ArrayList<Post> posts;
    private PostAdapterProfile postAdapterProfile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        posts = new ArrayList<Post>();
        postAdapterProfile = new PostAdapterProfile(posts);
        binding.frgProfileRecyclerView.setAdapter(postAdapterProfile);
        binding.frgProfileRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        posts.add(new Post(1,"https://raw.githubusercontent.com/DevTides/DogsApi/master/1.jpg"));
        postAdapterProfile.notifyDataSetChanged();


    }



//    public void navToFollow(View view, List<User> users) {
//        ProfileFragmentDirections.ProfileToFollow profileToFollow = ProfileFragmentDirections.profileToFollow(users.toArray(new User[0]));
//        Navigation.findNavController(view).navigate(profileToFollow);
//    }
//
//    public void follow(User user) {
//        viewModel.follow(user.getUser_id());
//
//        // update ui
//        viewModel.getUserDetailsById(user.getUser_id());
//    }
//
//    public void unfollow(int user_id) {
//        viewModel.unfollow(user_id);
//
//        // update ui
//        viewModel.getUserDetailsById(user_id);
//    }
//
//    public void showProfilePrivateError() {
//        Toast.makeText(requireContext(), getString(R.string.fragment_profile_msg_private), Toast.LENGTH_SHORT).show();
//    }
}
