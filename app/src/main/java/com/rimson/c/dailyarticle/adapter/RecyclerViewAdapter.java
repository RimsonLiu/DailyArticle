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

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<Voice> voiceArrayList;
    private Context context;

    private OnMyItemClickListener listener;

    public interface OnMyItemClickListener{
        void MyClick(View view,int position);
    }

    public void setOnMyItemClickListener(OnMyItemClickListener listener){
        this.listener=listener;
    }

    public RecyclerViewAdapter(Context context, ArrayList voiceArrayList){
        this.context=context;
        this.voiceArrayList=voiceArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view,parent,false);
        context=parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Voice thisVoice=voiceArrayList.get(position);

        holder.numberTV.setText(thisVoice.number);
        holder.titleTV.setText(thisVoice.title);
        holder.authorTV.setText(thisVoice.author);

        Glide.with(context)
                .load(thisVoice.imgURL)
                .apply(new RequestOptions()
                        .fitCenter())
                .into(holder.imageView);

        if (listener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.MyClick(view,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return voiceArrayList.size();
    }
}
