package com.rimson.c.dailyarticle.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rimson.c.dailyarticle.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class ArticleFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_article,container,false);

        @SuppressLint("HandlerLeak") Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {

                if(msg.what==1){
                    Bundle bundle=msg.getData();
                    String title=bundle.getString("TITLE");
                    String text=bundle.getString("TEXT");
                }
            }
        };

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Document document=Jsoup.connect("https://meiriyiwen.com/").get();
                    Element element = document.getElementById("article_show");
                    Element textBody=document.getElementsByClass("article_text").get(0);
                    String title=element.getElementsByTag("h1").get(0).text();
                    String author=element.getElementsByClass("article_author").get(0)
                            .getElementsByTag("p").get(0).text();

                    Log.d("hehe","123");
                    Log.d("hehe",author);

                    Bundle bundle=new Bundle();
                    bundle.putString("TITLE",title);
                    bundle.putString("TEXT",author);

                    Message message=new Message();
                    message.setData(bundle);
                    message.what=1;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        return view;
    }
}
