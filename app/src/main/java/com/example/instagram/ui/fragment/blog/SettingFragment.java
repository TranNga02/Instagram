package com.example.instagram.ui.fragment.blog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.ActivityNavigatorDestinationBuilderKt;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.databinding.FragmentCommentsBinding;
import com.example.instagram.databinding.FragmentSettingBinding;
import com.example.instagram.ui.model.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SettingFragment extends Fragment {
    private FragmentSettingBinding binding;
    UserProfile userProfile;
    private Uri uri;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("profiles").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserProfile userProfile = documentSnapshot.toObject(UserProfile.class);
                userProfile.setId(documentSnapshot.getId());
//                binding.frgSettingsTxtUserEmail.setText(userProfile.getEmail());
                binding.frgSettingsTxtUserBio.setText(userProfile.getBio());
                binding.frgSettingsTxtUserFullName.setText(userProfile.getFullname());
                binding.tvLabel.setText(userProfile.getUsername());
                if (userProfile.getAvatar() != null) {
                    Glide.with(getContext()).load(userProfile.getAvatar()).into(binding.frgSettingsImgProfilePhoto);
                }
            }
        });
        binding.frgSettingsLblRemovePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("profiles").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        db.collection("profiles").document(firebaseUser.getUid()).update("avatar", null);
                        binding.frgSettingsImgProfilePhoto.setImageResource(R.drawable.default_user_avatar);
                    }
                });
            }
        });
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


        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Uploading image...");
                        progressDialog.show();

                        // Tạo tham chiếu đến nơi bạn muốn lưu trữ ảnh trên Firebase Storage
                    StorageReference storageReference = storage.getReference().child("avatars/" + uri.getLastPathSegment());
                    // Upload ảnh lên Firebase Storage
                        storageReference.putFile(uri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // Ảnh đã được upload thành công lên Firebase Storage
                                        // Lấy URL của ảnh để lưu vào Firebase Database hoặc hiển thị lên màn hình
                                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri downloadUrl) {
                                                // downloadUrl chứa URL của ảnh đã được upload lên Firebase Storage
                                                Glide.with(getContext()).load(uri.toString()).into(binding.frgSettingsImgProfilePhoto);
                                                db.collection("profiles").document(firebaseUser.getUid()).update("avatar", downloadUrl.toString());
                                                progressDialog.dismiss();

                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Xử lý lỗi trong trường hợp upload ảnh không thành công
                                    }
                                });

                    }
                });
        binding.frgSettingsImgProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });
//        binding.frgSettingsTxtUserEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.frgSettingsTxtUserEmail.setText("");
//            }
//        });
        binding.frgSettingsTxtUserBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.frgSettingsTxtUserBio.setText("");
            }
        });
        binding.frgSettingsTxtUserFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.frgSettingsTxtUserFullName.setText("");
            }
        });
        binding.frgSettingsBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateProfile();
            }
        });

        return binding.getRoot();
    }
    boolean checkValid(){
//        if(binding.frgSettingsTxtUserEmail.getText().toString()==""){
//            Toast.makeText(getContext(), "Email is empty!", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        else if(binding.frgSettingsTxtUserPassword.getText().toString()==""){
//            Toast.makeText(getContext(), "Password is empty!", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        else if(binding.frgSettingsTxtUserPassword.getText().length()<7){
//            Toast.makeText(getContext(), "Password is more 6 characters!!", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if(binding.frgSettingsTxtUserFullName.getText().toString()==""){
            Toast.makeText(getContext(), "Name is empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(binding.frgSettingsTxtUserBio.getText().toString()==""){
            Toast.makeText(getContext(), "Bio is empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }
    public void updateProfile(){
        if(checkValid()){
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Uploading profiles...");
            progressDialog.show();
            String userId = firebaseUser.getUid();
            CollectionReference db = FirebaseFirestore.getInstance().collection("profiles");
            db.document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    // Tạo một HashMap chứa các cặp key-value của các trường cần cập nhật
                    Map<String, Object> updates = new HashMap<>();
//                    updates.put("email", binding.frgSettingsTxtUserEmail.getText().toString());
                    updates.put("bio", binding.frgSettingsTxtUserBio.getText().toString());
                    updates.put("fullname", binding.frgSettingsTxtUserFullName.getText().toString());

                    // Cập nhật các trường bằng phương thức update()
                    db.document(userId).update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    });
                }
            });
        }
    }
}
