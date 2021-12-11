package com.bryant.projectmdpai.Class;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Question implements Parcelable {
    private String id;
    private String author;
    private String title;
    private String question;
    private int status;
    private String time;

    public Question(){

    }
    public Question(String id, String author,String title, String question, String time) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.question = question;
        this.status = 1;
        this.time = time;

    }

    protected Question(Parcel in) {
        id = in.readString();
        author = in.readString();
        title = in.readString();
        question = in.readString();
        status = in.readInt();
        time = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
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

    public String getQuestion() {
        return question;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
        parcel.writeString(question);
        parcel.writeInt(status);
        parcel.writeString(time);
    }
}
