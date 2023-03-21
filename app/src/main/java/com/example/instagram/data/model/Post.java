package com.example.instagram.data.model;

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

    @SerializedName("comments")
    private List<Comment> comments;

    public Post(Integer post_id, String post_photo) {
        this.post_id = post_id;
        this.post_photo = post_photo;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public String getPost_photo() {
        return post_photo;
    }
}
