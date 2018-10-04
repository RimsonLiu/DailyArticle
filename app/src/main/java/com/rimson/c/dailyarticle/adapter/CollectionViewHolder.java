package com.rimson.c.dailyarticle.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rimson.c.dailyarticle.R;

public class CollectionViewHolder extends RecyclerView.ViewHolder {
    public TextView titleTV,authorTV;
    public ImageView imageView;

    public CollectionViewHolder(View itemView) {
        super(itemView);
        titleTV=(TextView)itemView.findViewById(R.id.cltTitle);
        authorTV=(TextView)itemView.findViewById(R.id.cltAuthor);
        imageView=(ImageView)itemView.findViewById(R.id.star);
    }
}
