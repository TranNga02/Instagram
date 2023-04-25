package com.example.instagram.ui.activity;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.instagram.R;
import com.example.instagram.databinding.ActivityMainBinding;
import com.example.instagram.repository.CommentRepository;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.navBottom.setVisibility(View.VISIBLE); // Show bottom navigation

        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        NavigationUI.setupWithNavController(binding.navBottom, navController);

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference commentsRef = db.collection("comments");
//
//        commentsRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    document.getReference().delete()
//                            .addOnSuccessListener(aVoid -> Log.d(TAG, "Document deleted successfully"))
//                            .addOnFailureListener(e -> Log.e(TAG, "Error deleting document", e));
//                }
//            } else {
//                Log.e(TAG, "Error getting documents: ", task.getException());
//            }
//        });


//
//            CommentRepository commentRepository = new CommentRepository();
//
//            commentRepository.addComment("chao ban", "ja9lUc12jNeKAN1TS1AW",
//                    "i9mWhS0OyqUz5GM163Wfwp0CvF42", Timestamp.valueOf("2023-03-29 08:25:41.000000000"));
//
//        commentRepository.addComment("chao ban", "ja9lUc12jNeKAN1TS1AW",
//                "e3ROeB2TTtbmrHHoRxAhK8sOyFw2", Timestamp.valueOf("2023-03-30 18:17:53.000000000"));
//        commentRepository.addComment("chao ban", "ja9lUc12jNeKAN1TS1AW",
//                "zulAOvUszkaAjhMGzJBe61OI9Bs2", Timestamp.valueOf("2023-04-02 03:42:28.000000000"));
//        commentRepository.addComment("chao ban", "ja9lUc12jNeKAN1TS1AW",
//                "i9mWhS0OyqUz5GM163Wfwp0CvF42", Timestamp.valueOf("2023-04-06 21:18:05.000000000"));
//        commentRepository.addComment("chao ban", "ja9lUc12jNeKAN1TS1AW",
//                "zulAOvUszkaAjhMGzJBe61OI9Bs2", Timestamp.valueOf("2023-04-09 05:35:29.000000000"));
//
//        commentRepository.addComment("chao ban", "uYnAd140PBwqLOwx1aPw",
//                "e3ROeB2TTtbmrHHoRxAhK8sOyFw2", Timestamp.valueOf("2023-03-31 11:49:06.000000000"));
//        commentRepository.addComment("chao ban", "uYnAd140PBwqLOwx1aPw",
//                "e3ROeB2TTtbmrHHoRxAhK8sOyFw2", Timestamp.valueOf("2023-04-10 12:52:46.000000000"));
//
//        commentRepository.addComment("chao ban", "uYnAd140PBwqLOwx1aPw",
//                "e3ROeB2TTtbmrHHoRxAhK8sOyFw2", Timestamp.valueOf("2023-04-12 19:19:07.000000000"));
//        commentRepository.addComment("chao ban", "uYnAd140PBwqLOwx1aPw",
//                "i9mWhS0OyqUz5GM163Wfwp0CvF42", Timestamp.valueOf("2023-04-14 04:33:59.000000000"));
//
//        commentRepository.addComment("chao ban", "I9GSxht46b7B5mzFhrfJ",
//                "i9mWhS0OyqUz5GM163Wfwp0CvF42", Timestamp.valueOf("2023-03-31 16:08:30.000000000"));
//        commentRepository.addComment("chao ban", "I9GSxht46b7B5mzFhrfJ",
//                "e3ROeB2TTtbmrHHoRxAhK8sOyFw2", Timestamp.valueOf("2023-03-31 21:55:12.000000000"));
//        commentRepository.addComment("chao ban", "I9GSxht46b7B5mzFhrfJ",
//                "e3ROeB2TTtbmrHHoRxAhK8sOyFw2", Timestamp.valueOf("2023-04-15 11:50:34.000000000"));
//        commentRepository.addComment("chao ban", "I9GSxht46b7B5mzFhrfJ",
//                "i9mWhS0OyqUz5GM163Wfwp0CvF42", Timestamp.valueOf("2023-04-17 20:07:11.000000000"));
//        commentRepository.addComment("chao ban", "I9GSxht46b7B5mzFhrfJ",
//                "zulAOvUszkaAjhMGzJBe61OI9Bs2", Timestamp.valueOf("2023-04-22 09:52:43.000000000"));
//

    }
}