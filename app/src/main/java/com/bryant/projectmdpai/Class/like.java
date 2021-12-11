package com.bryant.projectmdpai.Class;

import android.os.Parcel;
import android.os.Parcelable;

public class like implements Parcelable {
    private String id_article;
    private int like;

    public like(String id_article, int like) {
        this.id_article = id_article;

        this.like = like;
    }

    protected like(Parcel in) {
        id_article = in.readString();
        like = in.readInt();

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

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id_article);
        parcel.writeInt(like);
    }
}
