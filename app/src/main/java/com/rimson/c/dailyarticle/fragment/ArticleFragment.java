package com.rimson.c.dailyarticle.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rimson.c.dailyarticle.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ArticleFragment extends Fragment {
    private String title;
    private String author;

    private String articleURL="https://meiriyiwen.com/";

    private TextView titleTV;
    private TextView articleTV;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        View view=inflater.inflate(R.layout.fragment_article,container,false);

        titleTV=(TextView)view.findViewById(R.id.article_title);
        articleTV=(TextView)view.findViewById(R.id.article);

        //随机文章
        Button rollBtn=(Button)view.findViewById(R.id.roll);
        rollBtn.setText("随机文章");
        rollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                articleURL="https://meiriyiwen.com/random";
                getArticle(articleURL);
            }
        });

        //点击回到顶部
        final ScrollView scrollView=(ScrollView)view.findViewById(R.id.scroll_view);
        Button scrollBtn=(Button)view.findViewById(R.id.scrollUp);
        scrollBtn.setText("回到顶部");
        scrollBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                scrollView.fullScroll(View.FOCUS_UP);
            }
        } );

        getArticle(articleURL);//获取文章内容


        return view;
    }

    //获取文章内容
    public void getArticle(final String Url){
        @SuppressLint("HandlerLeak") final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {

                if(msg.what==1){
                    Bundle bundle=msg.getData();
                    String title=bundle.getString("TITLE")+"\n";
                    String author=bundle.getString("AUTHOR");
                    String article=author+bundle.getString("ARTICLE");
                    titleTV.setText(title);
                    articleTV.setText(article);
                    articleTV.setMovementMethod(ScrollingMovementMethod.getInstance());
                }
            }
        };

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Document document = Jsoup.connect(Url).get();
                    Element element = document.getElementById("article_show");
                    Elements elements=document.getElementsByClass("article_text")
                            .get(0).select("p");
                    title=element.getElementsByTag("h1").get(0).text();
                    author=element.getElementsByClass("article_author").get(0)
                            .getElementsByTag("p").get(0).text();

                    StringBuilder articleBuilder= new StringBuilder();
                    for(Element e:elements){
                        if (!e.text().isEmpty()){
                            articleBuilder.append("\n\n").append(e.text());
                        }
                    }

                    Bundle bundle=new Bundle();
                    bundle.putString("TITLE",title);
                    bundle.putString("AUTHOR",author);
                    bundle.putString("ARTICLE", articleBuilder.toString());

                    Message message=new Message();
                    message.setData(bundle);
                    message.what=1;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


}
