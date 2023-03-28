package com.example.instagram.ui.fragment.blog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.instagram.R;
import com.example.instagram.ui.fragment.setting.SettingFragment;

public class BlogFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog, container, false);
        Button btnSetting = view.findViewById(R.id.btn_setting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // Tạo một đối tượng Fragment mới của Fragment mà bạn muốn chuyển đến
//                SettingFragment newFragment = new SettingFragment();
//
//                // Lấy FragmentManager
//                FragmentManager fragmentManager = getFragmentManager();
//
//                // Bắt đầu một transaction Fragment
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                // Thay thế Fragment hiện tại bằng Fragment mới
//                fragmentTransaction.replace(R.id.fragmentContainerView, newFragment);
//
//                // Đặt Fragment hiện tại vào Back Stack
//                fragmentTransaction.addToBackStack(null);
//
//                // Hoàn thành transaction Fragment
//                fragmentTransaction.commit();


                NavController navController = Navigation.findNavController(getActivity(), R.id.fragmentContainerView);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.blogFragment, true)
                        .build();
                navController.navigate(R.id.settingFragment, null, navOptions);
            }
        });
        return view;
    }
}