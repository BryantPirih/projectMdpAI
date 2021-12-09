package com.bryant.projectmdpai.Class;

import android.os.Parcel;
import android.os.Parcelable;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Article implements Parcelable {
    private String id;
    private String author;
    private String title;
    private String content;
    private String time;

    public Article(String id, String author, String title, String content) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
    }

    public Article(String id, String author, String title, String content, String time) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.time = time;
    }

    protected Article(Parcel in) {
        id = in.readString();
        author = in.readString();
        title = in.readString();
        content = in.readString();
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
        DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd, HH:mm");
        return formatter.format(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(author);
        parcel.writeString(title);
        parcel.writeString(content);
    }
}
