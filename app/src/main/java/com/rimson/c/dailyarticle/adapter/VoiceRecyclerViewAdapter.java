package com.rimson.c.dailyarticle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rimson.c.dailyarticle.R;
import com.rimson.c.dailyarticle.bean.Voice;
import com.rimson.c.dailyarticle.uitl.BitmapUtil;

import java.util.ArrayList;

public class VoiceRecyclerViewAdapter extends RecyclerView.Adapter<VoiceViewHolder> {
    private ArrayList<Voice> voiceArrayList;

    private OnMyItemClickListener listener;

    public interface OnMyItemClickListener {
        void MyClick(View view, int position);
    }

    public void setOnMyItemClickListener(OnMyItemClickListener listener) {
        this.listener = listener;
    }

    public VoiceRecyclerViewAdapter(ArrayList<Voice> voiceArrayList) {
        this.voiceArrayList = voiceArrayList;
    }

    @NonNull
    @Override
    public VoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_voice, parent, false);
        return new VoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VoiceViewHolder holder, int position) {
        Voice thisVoice = voiceArrayList.get(position);

        holder.numberTV.setText(thisVoice.number);
        holder.titleTV.setText(thisVoice.title);
        holder.authorTV.setText(thisVoice.author);

        /*Glide.with(context)
                .load(thisVoice.imgURL)
                .apply(new RequestOptions()
                        .fitCenter())
                .into(holder.imageView);*/

        BitmapUtil bitmapUtil = new BitmapUtil();
        bitmapUtil.display(holder.imageView, thisVoice.imgURL);

        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.MyClick(view, holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return voiceArrayList.size();
    }
}
