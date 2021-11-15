package com.bryant.projectmdpai.Class;

import android.os.Parcel;
import android.os.Parcelable;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Article implements Parcelable {
    private int id;
    private int author;
    private String title;
    private String content;
    private Date time;
    private byte[] image;

    public Article(int id, int author, String title, String content) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        image=new byte[0];
    }

    protected Article(Parcel in) {
        id = in.readInt();
        author = in.readInt();
        title = in.readString();
        content = in.readString();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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
        parcel.writeInt(id);
        parcel.writeInt(author);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeByteArray(image);
    }
}
