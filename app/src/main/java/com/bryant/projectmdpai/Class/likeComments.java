package com.bryant.projectmdpai.Class;

import android.os.Parcel;
import android.os.Parcelable;

public class likeComments implements Parcelable {
    private int id_article;
    private int id_user;
    private int like;
    private String comment;

    public likeComments(int id_article, int id_user, int like, String comment) {
        this.id_article = id_article;
        this.id_user = id_user;
        this.like = like;
        this.comment = comment;
    }

    protected likeComments(Parcel in) {
        id_article = in.readInt();
        like = in.readInt();
        comment = in.readString();
        id_user = in.readInt();
    }

    public static final Creator<likeComments> CREATOR = new Creator<likeComments>() {
        @Override
        public likeComments createFromParcel(Parcel in) {
            return new likeComments(in);
        }

        @Override
        public likeComments[] newArray(int size) {
            return new likeComments[size];
        }
    };

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_article() {
        return id_article;
    }

    public void setId_article(int id_article) {
        this.id_article = id_article;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id_article);
        parcel.writeInt(like);
        parcel.writeString(comment);
        parcel.writeInt(id_user);
    }
}