package com.bryant.projectmdpai.Class;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private int id;
    private String username;
    private String email;
    private String address;
    private String password;
    private String role; // user/doctor
    private float rating; // 0 - 5.0

    public User() {

    }

    public User(String username, String email, String address, String password, String role, float rating) {
        this.username = username;
        this.email = email;
        this.address = address;
        this.password = password;
        this.role = role;
        this.rating = rating;
    }

    protected User(Parcel in) {
        id = in.readInt();
        username = in.readString();
        email = in.readString();
        address = in.readString();
        password = in.readString();
        role = in.readString();
        rating = in.readFloat();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeString(address);
        parcel.writeString(password);
        parcel.writeString(role);
        parcel.writeFloat(rating);
    }
}
