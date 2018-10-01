package com.rimson.c.dailyarticle.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Voice implements Parcelable {
    private String number;
    private String title;
    private String author;
    private String imgURL;
    private String swfURL;

    public Voice(String number,String title,String author,String imgURL,String swfURL){
        this.number=number;
        this.title=title;
        this.author=author;
        this.imgURL=imgURL;
        this.swfURL=swfURL;
    }

    private Voice(Parcel in) {
        number = in.readString();
        title = in.readString();
        author = in.readString();
        imgURL = in.readString();
        swfURL = in.readString();
    }

    public static final Creator<Voice> CREATOR = new Creator<Voice>() {
        @Override
        public Voice createFromParcel(Parcel in) {
            return new Voice(in);
        }

        @Override
        public Voice[] newArray(int size) {
            return new Voice[size];
        }
    };

    public String getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getSwfURL() {
        return swfURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(number);
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeString(imgURL);
        parcel.writeString(swfURL);
    }
}
