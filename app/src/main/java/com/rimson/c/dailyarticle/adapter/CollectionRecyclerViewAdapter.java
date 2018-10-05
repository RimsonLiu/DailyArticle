package com.rimson.c.dailyarticle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rimson.c.dailyarticle.R;
import com.rimson.c.dailyarticle.bean.Collection;

import java.util.ArrayList;

public class CollectionRecyclerViewAdapter extends RecyclerView.Adapter<CollectionViewHolder> {
    private ArrayList<Collection> collectionArrayList;
    private Context context;
    private OnCollectionClickListener listener;

    public interface OnCollectionClickListener{
        void collectionClick(View view,int position);
    }

    public void setOnCollectionClickListener(OnCollectionClickListener listener){
        this.listener=listener;
    }

    public CollectionRecyclerViewAdapter(Context context,ArrayList collectionArrayList){
        this.context=context;
        this.collectionArrayList=collectionArrayList;
    }

    public void update(ArrayList<Collection> list){
        this.collectionArrayList=list;
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_collection,parent,false);
        context=parent.getContext();
        return new CollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, final int position) {
        Collection thisCollection=collectionArrayList.get(position);

        holder.titleTV.setText(thisCollection.title);
        holder.authorTV.setText(thisCollection.author);

        if (listener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.collectionClick(v,position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return collectionArrayList.size();
    }
}
