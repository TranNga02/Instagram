package com.example.instagram.ui.fragment.feed;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.databinding.FragmentDetailPostBinding;
import com.example.instagram.databinding.FragmentMessageBinding;
import com.example.instagram.ui.adapter.UserAdapterSearch;
import com.example.instagram.ui.model.PostFeed;
import com.example.instagram.ui.model.User;
import com.example.instagram.ui.model.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MessageFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapterSearch userAdapterSearch;
    private ArrayList<UserProfile> users;
    private TextInputEditText search;
    private TextView username;
    private ImageButton back;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        navController = Navigation.findNavController(getActivity(), R.id.fragmentContainerView);
        recyclerView = view.findViewById(R.id.frgMessageRecyclerView);
        username = view.findViewById(R.id.tv_username_message);
        back = view.findViewById(R.id.btn_back_message);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        users = new ArrayList<>();
        userAdapterSearch = new UserAdapterSearch(getContext(), users, new UserAdapterSearch.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String idUser = users.get(position).getUsername();
                Bundle bundle = new Bundle();
                bundle.putString("username", idUser);
                bundle.putString("idUser", users.get(position).getId());
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.messageFragment, true)
                        .build();
                navController.navigate(R.id.detailMessageFragment, bundle, navOptions);
            }
        });
        recyclerView.setAdapter(userAdapterSearch);
        search = view.findViewById(R.id.frgMessageTxtUserName);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUser(s.toString().toLowerCase(Locale.ROOT));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.messageFragment, true)
                        .build();
                navController.navigate(R.id.feedFragment, null, navOptions);
            }
        });
        readUsers();
        return view;
    }
    private void searchUser(String s){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference profilesRef = db.collection("profiles");

        profilesRef.whereGreaterThanOrEqualTo("username", s)
                .whereLessThan("username", s + "\uf8ff")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            users.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                UserProfile user = document.toObject(UserProfile.class);
                                user.setId(document.getId());
                                if(user.getId().equals(FirebaseAuth.getInstance().getUid())){}
                                else {
                                    users.add(user);
                                }
                            }
                            userAdapterSearch.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }
    public void clickCardUser(View v) {
        Toast.makeText(getActivity(), "Layout", Toast.LENGTH_SHORT).show();
    }
    private void readUsers(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("profiles").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                username.setText(documentSnapshot.toObject(UserProfile.class).getUsername());
            }
        });
        db.collection("profiles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    users.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        UserProfile user = document.toObject(UserProfile.class); // create a new Post instance using the data from the document
                        user.setId(document.getId());
                        if(user.getId().equals(FirebaseAuth.getInstance().getUid())){}
                        else {
                            users.add(user);
                        }
                    }
                    userAdapterSearch.notifyDataSetChanged();


                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }


}