package com.rimson.c.dailyarticle.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rimson.c.dailyarticle.R;

public class VoiceViewHolder extends RecyclerView.ViewHolder {
    public TextView numberTV,titleTV,authorTV;
    public ImageView imageView;

    public VoiceViewHolder(View itemView) {
        super(itemView);
        numberTV=(TextView)itemView.findViewById(R.id.numberTV);
        titleTV=(TextView)itemView.findViewById(R.id.titleTV);
        authorTV=(TextView)itemView.findViewById(R.id.authorTV);
        imageView=(ImageView)itemView.findViewById(R.id.img);
    }
}
