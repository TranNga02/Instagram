package com.example.instagram.repository;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.instagram.ui.model.PostFeed;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostRepository {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getFullPost(final PostCallback callback) {
        ArrayList<PostFeed> postArrayList = new ArrayList<>();
        String userId = FirebaseAuth.getInstance().getUid();

        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PostFeed post = document.toObject(PostFeed.class);
                                String postUserId = document.getString("userId");
                                post.setUserId(postUserId);
                                post.setId(document.getId());

                                // Lấy thông tin người dùng ứng với user-id của comment
                                DocumentReference userDocRef = db.collection("profiles").document(postUserId);
                                userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> userTask) {
                                        if (userTask.isSuccessful()) {
                                            DocumentSnapshot userSnapshot = userTask.getResult();
                                            if (userSnapshot.exists()) {
                                                String username = userSnapshot.getString("username");
                                                String avatar = userSnapshot.getString("avatar");
                                                post.setUsername(username);
                                                post.setAvatar(avatar);
                                            } else {
                                                System.out.println("Không tìm thấy dữ liệu người dùng ứng với userId: " + postUserId);
                                            }
                                        } else {
                                            System.out.println("Lỗi khi lấy dữ liệu người dùng: " + userTask.getException());
                                        }

                                        // Sau khi hoàn thành việc lấy thông tin người dùng, cập nhật dữ liệu vào postArrayList
                                        postArrayList.add(post);

                                        // Kiểm tra xem đã lấy thông tin cho hết các bài post chưa
                                        if (postArrayList.size() == task.getResult().size()) {
                                            // Nếu đã lấy thông tin cho hết các bài post, gọi callback để thông báo hoàn thành
                                            // Sắp xếp commentArrayList theo thời gian (time)
                                            Collections.sort(postArrayList, new Comparator<PostFeed>() {
                                                @Override
                                                public int compare(PostFeed o1, PostFeed o2) {
                                                    return o2.getTime().compareTo(o1.getTime());
                                                }
                                            });
                                            callback.onPostsLoaded(postArrayList);
                                        }
                                    }
                                });
                            }
//                        } else {
//                            System.out.println("Error getting documents." + task.getException());
//                        }}
                        }
                    }
                });
    }

    public void getPostById(String postId, final PostCallback callback){
        ArrayList<PostFeed> postArrayList = new ArrayList<>();
        String userId = FirebaseAuth.getInstance().getUid();

        db.collection("posts").document(postId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                PostFeed post = documentSnapshot.toObject(PostFeed.class);
                // Lấy thông tin người dùng ứng với userId
                DocumentReference userDocRef = db.collection("profiles").document(post.getUserId());
                userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> userTask) {
                        if (userTask.isSuccessful()) {
                            DocumentSnapshot userSnapshot = userTask.getResult();
                            if (userSnapshot.exists()) {
                                String username = userSnapshot.getString("username");
                                String avatar = userSnapshot.getString("avatar");
                                post.setUsername(username);
                                post.setAvatar(avatar);
                            } else {
                                System.out.println("Không tìm thấy dữ liệu người dùng ứng với userId: " + post.getUserId());
                            }
                        } else {
                            System.out.println("Lỗi khi lấy dữ liệu người dùng: " + userTask.getException());
                        }

                        // Sau khi hoàn thành việc lấy thông tin người dùng, cập nhật dữ liệu vào postArrayList
                        postArrayList.add(post);

                        callback.onPostsLoaded(postArrayList);
                    }
                });
            }
        });
    }

    public void updateLikeOfPost(String postId, String userId){
        DocumentReference postRef = db.collection("posts").document(postId);

        postRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Get the current likes array
                Object likesObject = documentSnapshot.get("likes");
                if (likesObject instanceof ArrayList) {
                    ArrayList<String> likesArray = (ArrayList<String>) likesObject;
                    if (likesArray.contains(userId)) {
                        // If userId is already in the likes array, remove it
                        postRef.update("likes", FieldValue.arrayRemove(userId))
                                .addOnSuccessListener(aVoid -> {
                                    // If userId was removed successfully
                                    System.out.println("Successfully removed userId from likes array.");
                                })
                                .addOnFailureListener(e -> {
                                    // If there was an error removing userId from likes array, handle it here
                                    System.err.println("Error removing userId from likes array: " + e.getMessage());
                                });
                    } else {
                        // If userId is not in the likes array, add it
                        postRef.update("likes", FieldValue.arrayUnion(userId))
                                .addOnSuccessListener(aVoid -> {
                                    // If userId was added successfully
                                    System.out.println("Successfully added userId to likes array.");
                                })
                                .addOnFailureListener(e -> {
                                    // If there was an error adding userId to likes array, handle it here
                                    System.err.println("Error adding userId to likes array: " + e.getMessage());
                                });
                    }
                }
            }
        }).addOnFailureListener(e -> {
            // If there was an error getting the document, handle it here
            System.err.println("Error getting post document: " + e.getMessage());
        });
    }

    public void addNewPost(Context context, String content, Uri imageUri){

        // ------UploadImageToStorage------
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a storage reference to the video file
        String imageTitle = getTitleFromUri(context, imageUri);

        StorageReference imageRef = storage.getReference().child(imageTitle);

        // Create a new UploadTask to upload the video file
        UploadTask uploadTask = imageRef.putFile(imageUri);

        // Set up a listener for the upload progress
        uploadTask.addOnProgressListener(taskSnapshot -> {
            Log.d("MY_APP", "Uploading");
        }).addOnPausedListener(taskSnapshot -> {
            Log.d("MY_APP", "Upload is paused");
        }).addOnFailureListener(exception -> {
            Log.e("DEBUG", "Failed to upload video", exception);
        }).addOnSuccessListener(taskSnapshot -> {
            Log.d("DEBUG", "Video uploaded successfully");

            // Get the download URL of the uploaded video
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                Log.d("DEBUG", "Video URL: " + imageUrl);

                // Do something with the video URL, like storing it in a Firestore document or displaying it in a VideoView

                UpLoadPostInfoToFireStore(new Date(), imageUrl, content);

            }).addOnFailureListener(exception -> {
                Log.e("DEBUG", "Failed to get download URL for video", exception);
            });
        });
    }

    public String getTitleFromUri(Context context, Uri uri){
        String imageTitle = "";
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int titleIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (titleIndex >= 0) {
                imageTitle = cursor.getString(titleIndex);
            }
            cursor.close();
        }
        return imageTitle;
    }

    private void UpLoadPostInfoToFireStore(Date date, String imageUrl, String content) {
        DocumentReference postsRef = db.collection("posts").document();
        List<String> srcs = Arrays.asList(imageUrl);

        Map<String, Object> postData = new HashMap<>();
        postData.put("content", content);
        postData.put("likes", Arrays.asList());
        postData.put("src", srcs);
        postData.put("time", date);
        postData.put("userId", FirebaseAuth.getInstance().getUid());

        postsRef.set(postData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document added successfully
                        Log.d("TAG", "Post document added with ID: " + postsRef.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add document
                        Log.e("TAG", "Error adding post document", e);
                    }
                });
    }

    public interface PostCallback {
        void onPostsLoaded(ArrayList<PostFeed> postsList);
    }
}
