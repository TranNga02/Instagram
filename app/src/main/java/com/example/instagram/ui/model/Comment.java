package com.example.instagram.ui.model;

import com.google.firebase.Timestamp;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class Comment {
    private String id;
    private String userId;
    private String postId;
    private List<String> likes;
    private String content;
    private Timestamp time;
    private String username;
    private String avatar;

    public int getNumberofDays(){
        // Lấy ngày hiện tại
        Date currentDate = new Date();

        // Chuyển đổi Timestamp thành Date
        Date timestampDate = time.toDate();

        // Tính số milliseconds giữa ngày hiện tại và ngày của Timestamp
        long timeDifferenceInMillis = Math.abs(currentDate.getTime() - timestampDate.getTime());

        // Chuyển đổi số milliseconds thành số ngày
        return (int) (timeDifferenceInMillis / (24 * 60 * 60 * 1000));
    }

    public boolean isLike(String userId){
        if(this.likes.contains(userId)){
            return true;
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public List<String> getLikes() {
        return likes;
    }

    public int countLikes() {
        return likes.size();
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
