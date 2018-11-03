package com.rimson.c.dailyarticle.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rimson.c.dailyarticle.R;

public class VoiceViewHolder extends RecyclerView.ViewHolder {
    public TextView numberTV, titleTV, authorTV;
    ImageView imageView;

    VoiceViewHolder(View itemView) {
        super(itemView);
        numberTV = itemView.findViewById(R.id.numberTV);
        titleTV = itemView.findViewById(R.id.titleTV);
        authorTV = itemView.findViewById(R.id.authorTV);
        imageView = itemView.findViewById(R.id.img);
    }
}
