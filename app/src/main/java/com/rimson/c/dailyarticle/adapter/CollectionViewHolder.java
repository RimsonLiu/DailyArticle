package com.rimson.c.dailyarticle.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rimson.c.dailyarticle.R;

public class CollectionViewHolder extends RecyclerView.ViewHolder {
    public TextView titleTV, authorTV;

    CollectionViewHolder(View itemView) {
        super(itemView);
        titleTV = itemView.findViewById(R.id.cltTitle);
        authorTV = itemView.findViewById(R.id.cltAuthor);
        ImageView imageView = itemView.findViewById(R.id.cltStar);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
