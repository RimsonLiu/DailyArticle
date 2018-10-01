package com.rimson.c.dailyarticle.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rimson.c.dailyarticle.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    private TextView number,title,author;
    private ImageView image;

    public ViewHolder(View itemView) {
        super(itemView);
        number=(TextView)itemView.findViewById(R.id.numberTV);
        title=(TextView)itemView.findViewById(R.id.titleTV);
        author=(TextView)itemView.findViewById(R.id.authorTV);
        image=(ImageView)itemView.findViewById(R.id.img);
    }

    public TextView getNumber() {
        return number;
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getAuthor() {
        return author;
    }

    public ImageView getImage() {
        return image;
    }
}
