package com.rimson.c.dailyarticle.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rimson.c.dailyarticle.R;
import com.rimson.c.dailyarticle.activity.VoiceActivity;
import com.rimson.c.dailyarticle.adapter.VoiceRecyclerViewAdapter;
import com.rimson.c.dailyarticle.bean.Voice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Base64;

public class VoiceFragment extends Fragment {
    private ArrayList<Voice> voiceArrayList;
    private ArrayList<Voice> list=new ArrayList<>();

    private String Url="http://voice.meiriyiwen.com/";

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_voice,container,false);

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);

        getVoice();//获取列表信息

        return view;
    }

    //获取列表信息
    private void getVoice(){
        @SuppressLint("HandlerLeak") final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1){
                    final Context context=getActivity();
                    Bundle bundle=msg.getData();
                    voiceArrayList=bundle.getParcelableArrayList("LIST");

                    LinearLayoutManager layoutManager=new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(layoutManager);
                    VoiceRecyclerViewAdapter adapter=new VoiceRecyclerViewAdapter(context,voiceArrayList);
                    recyclerView.setAdapter(adapter);

                    adapter.setOnMyItemClickListener(new VoiceRecyclerViewAdapter.OnMyItemClickListener() {
                        @Override
                        public void MyClick(View view, int position) {
                            Voice voice=voiceArrayList.get(position);
                            Intent intent=new Intent(context,VoiceActivity.class);
                            intent.putExtra("VOICE",voice);
                            startActivity(intent);
                        }
                    });
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc=  Jsoup.connect("http://voice.meiriyiwen.com/").get();

                    Elements elements=doc.body().getElementsByClass("list_box");
                    for (Element e:elements){

                        String number=e.select(".voice_tag").text();

                        String title=e.select(".list_author").get(0)
                                .select("a").text();

                        String author=e.select(".author_name").get(0)
                                .text();

                        String imgURL=e.select("a.box_list_img").select("img")
                                .attr("abs:src");

                        String newURL=e.select(".box_list_img")
                                .attr("abs:href");

                        Document swfDoc=Jsoup.connect(newURL).get();
                        String swfURL=swfDoc.body().select(".p_file").select("embed")
                                .attr("src");
                        String rex="=(.*?)&";
                        Pattern pattern = Pattern.compile(rex);// 匹配的模式
                        Matcher matcher = pattern.matcher(swfURL);
                        matcher.find();
                        String uncodedURL=matcher.group(1);
                        String mp3URL = new String(Base64.decode(uncodedURL,Base64.DEFAULT));

                        Voice voice=new Voice(number,title,author,imgURL,mp3URL);
                        list.add(voice);
                    }
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putParcelableArrayList("LIST",list);
                    message.setData(bundle);
                    message.what=1;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
