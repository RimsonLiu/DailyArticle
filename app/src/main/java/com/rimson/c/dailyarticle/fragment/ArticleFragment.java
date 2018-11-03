package com.rimson.c.dailyarticle.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.rimson.c.dailyarticle.R;
import com.rimson.c.dailyarticle.bean.Collection;
import com.rimson.c.dailyarticle.db.Operator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ArticleFragment extends Fragment {
    private String articleURL = "https://meiriyiwen.com/";
    private String title;
    private String author;
    private String article;
    private boolean articleStared;


    private TextView titleTV;
    private TextView articleTV;
    private ImageView starBtn;

    private Operator operator;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = initViews(inflater, container);
        getArticle(articleURL);//获取文章内容

        return view;
    }

    //初始化布局
    public View initViews(@Nullable LayoutInflater inflater, @Nullable ViewGroup container) {
        assert inflater != null;
        View view = inflater.inflate(R.layout.fragment_article, container, false);

        operator = new Operator(getContext());

        titleTV = view.findViewById(R.id.article_title);
        articleTV = view.findViewById(R.id.article);
        starBtn = view.findViewById(R.id.article_star);
        starBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title == null) {
                    Toast.makeText(getContext(), "收藏失败", Toast.LENGTH_SHORT).show();
                } else {
                    if (articleStared) {
                        articleStared = false;
                        operator.delete(new Collection("ARTICLE", title, author, null, null));
                        starBtn.setImageResource(R.drawable.star);
                        Toast.makeText(getContext(), "取消收藏", Toast.LENGTH_SHORT).show();
                    } else {
                        articleStared = true;
                        operator.add(new Collection("ARTICLE", title, author, article, ""));
                        starBtn.setImageResource(R.drawable.stared);
                        Toast.makeText(getContext(), "收藏成功", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        //随机文章
        Button rollBtn = view.findViewById(R.id.roll);
        rollBtn.setText("随机文章");
        rollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() != null) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getContext()
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager != null) {
                        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isAvailable()) {
                            articleURL = "https://meiriyiwen.com/random";
                            getArticle(articleURL);
                        } else {
                            Toast.makeText(getContext(), "当前网络不可用", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        //点击回到顶部
        final ScrollView scrollView = view.findViewById(R.id.scroll_view);
        Button scrollBtn = view.findViewById(R.id.scrollUp);
        scrollBtn.setText("回到顶部");
        scrollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.fullScroll(View.FOCUS_UP);
            }
        });

        return view;
    }

    //获取文章内容
    public void getArticle(final String Url) {
        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 1) {
                    Bundle bundle = msg.getData();
                    title = bundle.getString("TITLE") + "\n";
                    author = bundle.getString("AUTHOR");
                    article = bundle.getString("ARTICLE");

                    titleTV.setText(title);
                    String articleText = author + article;
                    articleTV.setText(articleText);
                    articleTV.setMovementMethod(ScrollingMovementMethod.getInstance());

                    //判断是否已经收藏
                    articleStared = operator.dataExists(new Collection("ARTICLE", title, author, null, null));
                    if (articleStared) {
                        starBtn.setImageResource(R.drawable.stared);
                    } else {
                        starBtn.setImageResource(R.drawable.star);
                    }
                }
            }
        };

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Document document = Jsoup.connect(Url).get();
                    Element element = document.getElementById("article_show");
                    Elements elements = document.getElementsByClass("article_text")
                            .get(0).select("p");
                    title = element.getElementsByTag("h1").get(0).text();
                    author = element.getElementsByClass("article_author").get(0)
                            .getElementsByTag("p").get(0).text();

                    StringBuilder articleBuilder = new StringBuilder();
                    for (Element e : elements) {
                        if (!e.text().isEmpty()) {
                            articleBuilder.append("\n\n").append(e.text());
                        }
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("TITLE", title);
                    bundle.putString("AUTHOR", author);
                    bundle.putString("ARTICLE", articleBuilder.toString());

                    Message message = new Message();
                    message.setData(bundle);
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


}
