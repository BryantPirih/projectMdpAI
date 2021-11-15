package com.bryant.projectmdpai.Class;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Question implements Parcelable {
    private int id;
    private int author;
    private String question;
    private boolean isActive;
    private Date time;

    public Question(int id, int author, String question) {
        this.id = id;
        this.author = author;
        this.question = question;
        isActive=true;
        time=Calendar.getInstance().getTime();
    }

    protected Question(Parcel in) {
        id = in.readInt();
        author = in.readInt();
        question = in.readString();
        isActive = in.readByte() != 0;
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
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
        parcel.writeInt(id);
        parcel.writeInt(author);
        parcel.writeString(question);
        parcel.writeByte((byte) (isActive ? 1 : 0));
    }
}
