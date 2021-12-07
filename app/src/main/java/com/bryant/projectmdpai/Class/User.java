package com.bryant.projectmdpai.Class;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String id;
    private String username;
    private String email;
    private String full_name;
    private String address;
    private String password;
    private String role; // user/doctor
    private float rating; // 0 - 5.0
    private int status; // 0 unavailable/unverified , 1 available,
    private String desc; //

    public User() {

    }
    public User( String username, String email, String full_name, String address, String password, String role, float rating,int status) {
        this.username = username;
        this.email = email;
        this.full_name = full_name;
        this.address = address;
        this.password = password;
        this.role = role;
        this.rating = rating;
        this.status = status;
        id="";
        desc="";
    }

    protected User(Parcel in) {
        id = in.readString();
        username = in.readString();
        email = in.readString();
        full_name = in.readString();
        address = in.readString();
        password = in.readString();
        role = in.readString();
        rating = in.readFloat();
        status = in.readInt();
        desc = in.readString();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeString(full_name);
        parcel.writeString(address);
        parcel.writeString(password);
        parcel.writeString(role);
        parcel.writeFloat(rating);
        parcel.writeInt(status);
        parcel.writeString(desc);
    }
}
