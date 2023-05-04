package com.example.instagram.ui.model;

import com.google.firebase.Timestamp;

public class Notification {
    private String id;
    private String makeUserId;
    private String postId;
    private String ownUserId;
    private String content;
    private Timestamp time;

    public String newLikeNoti(String userName){
        return  userName + " da thich bai viet cua ban.";
    }

    public String newCommentNoti(String userName){
        return  userName + " da binh luan ve bai viet cua ban.";
    }

    public String newFollowerNoti(String userName){
        return  userName + " da theo doi ban.";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMakeUserId() {
        return makeUserId;
    }

    public void setMakeUserId(String makeUserId) {
        this.makeUserId = makeUserId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getOwnUserId() {
        return ownUserId;
    }

    public void setOwnUserId(String ownUserId) {
        this.ownUserId = ownUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
