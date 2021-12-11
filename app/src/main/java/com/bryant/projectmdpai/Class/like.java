package com.bryant.projectmdpai.Class;

import android.os.Parcel;
import android.os.Parcelable;

public class like implements Parcelable {
    private String id_article;
    private String id_user;

    public like(String id_article, String id_user) {
        this.id_article = id_article;
        this.id_user = id_user;
    }

    protected like(Parcel in) {
        id_article = in.readString();
        id_user = in.readString();

    }
    public static final Creator<com.bryant.projectmdpai.Class.like> CREATOR = new Creator<com.bryant.projectmdpai.Class.like>() {
        @Override
        public com.bryant.projectmdpai.Class.like createFromParcel(Parcel in) {
            return new like(in);
        }
        @Override
        public com.bryant.projectmdpai.Class.like[] newArray(int size) {
            return new like[size];
        }
    };

    public String getId_article() {
        return id_article;
    }

    public void setId_article(String id_article) {
        this.id_article = id_article;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id_article);
        parcel.writeString(id_article);
    }
}
