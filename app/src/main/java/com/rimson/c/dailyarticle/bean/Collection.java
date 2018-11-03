package com.rimson.c.dailyarticle.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Collection implements Parcelable {
    public String type;
    public String title;
    public String author;
    public String content;
    public String url;

    //type:ARTICLE，文章，content为正文
    //type:VOICE，声音，content为路径
    public Collection(String type, String title, String author, String content, String URL) {
        this.type = type;
        this.title = title;
        this.author = author;
        this.content = content;
        this.url = URL;
    }

    public Collection() {

    }

    private Collection(Parcel in) {
        type = in.readString();
        title = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public static final Creator<Collection> CREATOR = new Creator<Collection>() {
        @Override
        public Collection createFromParcel(Parcel in) {
            return new Collection(in);
        }

        @Override
        public Collection[] newArray(int size) {
            return new Collection[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }
}
