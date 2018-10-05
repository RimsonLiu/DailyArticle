package com.rimson.c.dailyarticle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rimson.c.dailyarticle.R;
import com.rimson.c.dailyarticle.activity.ArticleActivity;
import com.rimson.c.dailyarticle.activity.MainActivity;
import com.rimson.c.dailyarticle.activity.VoiceActivity;
import com.rimson.c.dailyarticle.adapter.CollectionRecyclerViewAdapter;
import com.rimson.c.dailyarticle.bean.Collection;
import com.rimson.c.dailyarticle.bean.Voice;
import com.rimson.c.dailyarticle.db.Operator;

import java.util.ArrayList;

public class CollectionFragment extends Fragment {
    private ArrayList<Collection> collections;
    private Operator operator;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private CollectionRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_collection,container,false);

        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh);
        recyclerView=(RecyclerView) view.findViewById(R.id.collectionRV);

        initViews();
        return view;
    }

    private void initViews(){
        operator=new Operator(getContext());
        collections=operator.queryAll();
        final Context context=getContext();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                collections=operator.queryAll();
                adapter.update(collections);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new CollectionRecyclerViewAdapter(context,collections);
        recyclerView.setAdapter(adapter);

        adapter.setOnCollectionClickListener(new CollectionRecyclerViewAdapter.OnCollectionClickListener() {
            @Override
            public void collectionClick(View view, int position) {
                Collection collection=collections.get(position);
                if (collection.type.equals("ARTICLE")){
                    Intent intent=new Intent(context,ArticleActivity.class);
                    intent.putExtra("ARTICLE",collection);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(context,VoiceActivity.class);
                    Voice voice=new Voice(null,collection.title,collection.author,null,null);
                    intent.putExtra("VOICE",voice);
                    intent.putExtra("FROM","LOCAL");
                    startActivity(intent);
                }
            }
        });


    }

}
