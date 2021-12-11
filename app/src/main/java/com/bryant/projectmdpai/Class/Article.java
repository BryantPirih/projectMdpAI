package com.bryant.projectmdpai.Class;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Article implements Parcelable {
    private String id;
    private String id_author;
    private String author;
    private String title;
    private String content;
    private String time;
    private int jumlahLike;
    private int jumlahComment;
    private byte[] image;



    public Article(String id, String id_author, String author, String title, String content, String time, int jumlahLike, int jumlahComment, byte[] image) {
        this.id = id;
        this.id_author = id_author;
        this.author = author;
        this.title = title;
        this.content = content;
        this.time = time;
        this.jumlahLike = jumlahLike;
        this.jumlahComment = jumlahComment;
        this.image = image;
    }

    protected Article(Parcel in) {
        id = in.readString();
        id_author = in.readString();
        author = in.readString();
        title = in.readString();
        content = in.readString();
        time = in.readString();
        jumlahLike = in.readInt();
        jumlahComment = in.readInt();
        image = in.createByteArray();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public byte[] getImage() {
        return image;
    }

    public String getId_author() {
        return id_author;
    }

    public void setId_author(String id_author) {
        this.id_author = id_author;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getJumlahLike() {
        return jumlahLike;
    }

    public void setJumlahLike(int jumlahLike) {
        this.jumlahLike = jumlahLike;
    }

    public int getJumlahComment() {
        return jumlahComment;
    }

    public void setJumlahComment(int jumlahComment) {
        this.jumlahComment = jumlahComment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getTimeString(){
        return time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(id_author);
        parcel.writeString(author);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(time);
        parcel.writeInt(jumlahLike);
        parcel.writeInt(jumlahComment);
        parcel.writeByteArray(image);
    }
}
