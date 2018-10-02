package com.rimson.c.dailyarticle.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Voice implements Parcelable {
    public String number;
    public String title;
    public String author;
    public String imgURL;
    public String mp3URL;

    public Voice(String number,String title,String author,String imgURL,String mp3URL){
        this.number=number;
        this.title=title;
        this.author=author;
        this.imgURL=imgURL;
        this.mp3URL=mp3URL;
    }

    private Voice(Parcel in) {
        number = in.readString();
        title = in.readString();
        author = in.readString();
        imgURL = in.readString();
        mp3URL = in.readString();
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
        parcel.writeString(mp3URL);
    }
}
