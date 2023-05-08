package com.example.instagram.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.instagram.repository.NotificationRepository;
import com.example.instagram.repository.UserRepository;
import com.example.instagram.ui.model.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NotificationViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Notification>> notifications;
    private MutableLiveData<String> userAvatar;
    private NotificationRepository notificationRepo;
    private UserRepository userRepo;
    private CollectionReference notificationCollectionRef;
    private FirebaseFirestore firestore;
    private String userId;

    public NotificationViewModel() {
        notificationRepo = new NotificationRepository();
        userRepo = new UserRepository();
        firestore = FirebaseFirestore.getInstance();
        notificationCollectionRef = firestore.collection("notifications");
        userId = FirebaseAuth.getInstance().getUid();

        // Đăng ký lắng nghe sự kiện trên collection 'notification' trong Firestore
//        notificationCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
//                getAllNotifications();
//            }
//        });
    }

    public LiveData<ArrayList<Notification>> getNotifications(){
        if(notifications == null){
            notifications = new MutableLiveData<>();
            notifications.setValue(new ArrayList<>());
        }
        return notifications;
    }

    public LiveData<String> getUserAvatar(){
        if(userAvatar == null){
            userAvatar = new MutableLiveData<>();
            userAvatar.setValue(new String());
        }
        return userAvatar;
    }

    public void getAllNotifications(){
        notificationRepo.loadNotification(new NotificationRepository.NotificationCallBack() {
            @Override
            public void onNotificationsLoaded(ArrayList<Notification> notificationsArray) {
                notifications.setValue(notificationsArray);
            }
        });
    }

    public void getUserAvatarById(String userId){
        userRepo.getUserAvatar(userId, new UserRepository.UserCallback() {
            @Override
            public void onAvatarLoaded(String avatar) {
                userAvatar.setValue(avatar);
            }

            @Override
            public void onUsernameLoaded(String username) {}
        });
    }

    public void addNotification(String notificationFlag, String makeUserId, String postId,String ownUserId){
        final String[] content = new String[1];
        Notification notification = new Notification();

        userRepo.getCurrentUsername(new UserRepository.UserCallback() {
            @Override
            public void onAvatarLoaded(String avatar) {}

            @Override
            public void onUsernameLoaded(String username) {
                if(notificationFlag == "likePost") content[0] = notification.newLikePostNoti(username);
                else if(notificationFlag == "comment") content[0] = notification.newCommentNoti(username);
                else if(notificationFlag == "follow") content[0] = notification.newFollowerNoti(username);
                else if(notificationFlag == "likeComment") content[0] = notification.newLikeComment(username);
                notificationRepo.addNotification(content[0], makeUserId, postId, ownUserId);
            }
        });
    }
}
