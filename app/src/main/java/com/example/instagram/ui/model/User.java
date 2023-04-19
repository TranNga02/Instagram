package com.example.instagram.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User implements Parcelable {
    @SerializedName("Id")
    private Integer id;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("fullname")
    private String fullname;

    @SerializedName("photo")
    private String photo;

    @SerializedName("bio")
    private String bio;

    @SerializedName("profile_private")
    private Integer profile_private;

    @SerializedName("followers")
    private List<User> followers;

    @SerializedName("following")
    private List<User> following;

    public User() {
    }

    protected User(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        email = in.readString();
        username = in.readString();
        password = in.readString();
        fullname = in.readString();
        photo = in.readString();
        bio = in.readString();
        if (in.readByte() == 0) {
            profile_private = null;
        } else {
            profile_private = in.readInt();
        }
        followers = in.createTypedArrayList(User.CREATOR);
        following = in.createTypedArrayList(User.CREATOR);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public Boolean followerListContains(int id) {
        for (User u : followers) {
            if (u.id == id) {
                return true;
            }
        }

        return false;
    }

    public Integer getid() {
        return id;
    }

    public void setid(Integer id) {
        this.id = id;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getname() {
        return username;
    }

    public void setname(String name) {
        this.username = name;
    }

    public String getpassword() {
        return password;
    }

    public void setpassword(String password) {
        this.password = password;
    }

    public String getfullname() {
        return fullname;
    }

    public void setfullname(String fullname) {
        this.fullname = fullname;
    }

    public String getphoto() {
        return photo;
    }

    public void setphoto(String photo) {
        this.photo = photo;
    }

    public String getbio() {
        return bio;
    }

    public void setbio(String bio) {
        this.bio = bio;
    }

    public Integer getprofile_private() {
        return profile_private;
    }

    public void setprofile_private(Integer profile_private) {
        this.profile_private = profile_private;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public List<User> getFollowing() {
        return following;
    }

    public void setFollowing(List<User> following) {
        this.following = following;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(fullname);
        dest.writeString(photo);
        dest.writeString(bio);
        if (profile_private == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(profile_private);
        }
        dest.writeTypedList(followers);
        dest.writeTypedList(following);
    }
}
