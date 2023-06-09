package com.example.instagram.ui.model;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Post implements Serializable {
    @SerializedName("post_id")
    private Integer post_id;

    @SerializedName("post_photo")
    private String post_photo;

    @SerializedName("post_description")
    private String post_description;

    @SerializedName("post_owner")
    private User post_owner;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("likers")
    private List<User> likers;
    private List<String> src;
//    @SerializedName("comments")
//    private List<Comment> comments;

    public Post() {
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public String getPost_photo() {
        return (src.get(0));
    }

    public void setPost_photo(String post_photo) {
        this.post_photo = post_photo;
    }

    public String getPost_description() {
        return post_description;
    }

    public void setPost_description(String post_description) {
        this.post_description = post_description;
    }

    public User getPost_owner() {
        return post_owner;
    }

    public void setPost_owner(User post_owner) {
        this.post_owner = post_owner;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<User> getLikers() {
        return likers;
    }

    public void setLikers(List<User> likers) {
        this.likers = likers;
    }

//    public List<Comment> getComments() {
//        return comments;
//    }
//
//    public void setComments(List<Comment> comments) {
//        this.comments = comments;
//    }
    public List<String> getSrc() {
    return src;
}

    public void setSrc(List<String> src) {
        this.src = src;
    }
}
