package com.example.instagram.ui.fragment.feed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.databinding.FragmentDetailMessageBinding;
import com.example.instagram.databinding.FragmentMessageBinding;
import com.example.instagram.ui.adapter.MessageAdapter;
import com.example.instagram.ui.model.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class DetailMessageFragment extends Fragment {
    private FragmentDetailMessageBinding binding;
    String username, idReceiver;
    String senderRoom, receiverRoom;
    DatabaseReference databaseReferenceSender, databaseReferenceReceiver;
    MessageAdapter messageAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailMessageBinding.inflate(inflater, container, false);
        username = getArguments().getString("username");
        idReceiver = getArguments().getString("idUser");
        messageAdapter = new MessageAdapter(getContext());
        binding.rcvChat.setAdapter(messageAdapter);
        binding.rcvChat.setLayoutManager(new LinearLayoutManager(getContext()));

        senderRoom = FirebaseAuth.getInstance().getUid() + idReceiver;
        receiverRoom = idReceiver + FirebaseAuth.getInstance().getUid();
        databaseReferenceSender = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);
        databaseReferenceReceiver = FirebaseDatabase.getInstance().getReference("chats").child(receiverRoom);
        binding.tvUsernameMessage.setText(username);
        databaseReferenceSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageAdapter.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Message message = dataSnapshot.getValue(Message.class);
                    messageAdapter.Add(message);
                    binding.progressChat.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReferenceReceiver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageAdapter.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Message message = dataSnapshot.getValue(Message.class);
                    messageAdapter.Add(message);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.ivSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.editMessage.getText().toString();
                if(message.trim().length()>0){
                    sendMsg(message);
                    binding.editMessage.setText("");
                }
            }
        });
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.fragmentContainerView);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.detailMessageFragment, true)
                        .build();
                navController.navigate(R.id.messageFragment, null, navOptions);
            }
        });
        return binding.getRoot();
    }

    private void sendMsg(String message) {
        String idMsg = UUID.randomUUID().toString();
        Message message1 = new Message(idMsg, FirebaseAuth.getInstance().getUid(), message, new Date());
        messageAdapter.Add(message1);
        databaseReferenceSender.child(idMsg).setValue(message1);
        databaseReferenceReceiver.child(idMsg).setValue(message1);

    }

}