package com.bryant.projectmdpai.Class;

import android.os.Parcel;
import android.os.Parcelable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Answer implements Parcelable {
    private String id;
    private String question_id;
    private String author;
    private String answer;
    private String time;

    public Answer(){

    }

    public Answer(String id, String question_id, String author, String answer, String time) {
        this.id = id;
        this.question_id = question_id;
        this.author = author;
        this.answer = answer;
        this.time= time;
    }

    protected Answer(Parcel in) {
        id = in.readString();
        question_id = in.readString();
        author = in.readString();
        answer = in.readString();
        time = in.readString();
    }

    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public String getTimeString(){
        DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd, HH:mm");
        return formatter.format(time);
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
        parcel.writeString(question_id);
        parcel.writeString(author);
        parcel.writeString(answer);
        parcel.writeString(time);
    }
}
