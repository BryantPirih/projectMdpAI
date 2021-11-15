package com.bryant.projectmdpai.Class;

import android.os.Parcel;
import android.os.Parcelable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Answer implements Parcelable {
    private int id;
    private int question_id;
    private int author;
    private String answer;
    private Date time;

    public Answer(int id, int question_id, int author, String answer) {
        this.id = id;
        this.question_id = question_id;
        this.author = author;
        this.answer = answer;
        time= Calendar.getInstance().getTime();
    }

    protected Answer(Parcel in) {
        id = in.readInt();
        question_id = in.readInt();
        author = in.readInt();
        answer = in.readString();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(question_id);
        parcel.writeInt(author);
        parcel.writeString(answer);
    }
}
