package com.example.instagram.ui.model;

import java.util.List;

public class PostFeed {
    private String content;
    private String time;
    private String userId;
    private List<String> likes;
    private List<String> src;

    public PostFeed() {}

    public PostFeed(String content, String time, String userId, List<String> likes, List<String> src) {
        this.content = content;
        this.time = time;
        this.userId = userId;
        this.likes = likes;
        this.src = src;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public List<String> getSrc() {
        return src;
    }

    public void setSrc(List<String> src) {
        this.src = src;
    }
}
