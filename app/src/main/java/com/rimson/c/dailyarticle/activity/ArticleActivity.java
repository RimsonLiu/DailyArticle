package com.rimson.c.dailyarticle.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.rimson.c.dailyarticle.R;
import com.rimson.c.dailyarticle.bean.Collection;
import com.rimson.c.dailyarticle.db.Operator;

public class ArticleActivity extends AppCompatActivity {
    private boolean articleStared;
    private Operator operator;
    private Context mContext;

    private String title;
    private String author;
    private String article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        articleStared=true;
        mContext=this;
        getArticle();
    }

    private void getArticle(){
        final Collection collection=getIntent().getParcelableExtra("ARTICLE");
        this.title=collection.title;
        this.author=collection.author;
        this.article=collection.content;
        operator=new Operator(mContext);

        TextView titleTV=(TextView)findViewById(R.id.articleTitle);
        TextView articleTV=(TextView)findViewById(R.id.Article);
        final ImageView star=(ImageView)findViewById(R.id.articleStar);
        Button scrollBtn=(Button)findViewById(R.id.scrollUP);
        final ScrollView scrollView=(ScrollView)findViewById(R.id.article_scroll);

        titleTV.setText(collection.title);
        articleTV.setText(author+article);

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (articleStared){
                    articleStared=false;
                    operator.delete(new Collection("ARTICLE",title,author,null));
                    star.setImageResource(R.drawable.star);
                    Toast.makeText(mContext,"取消收藏",Toast.LENGTH_SHORT).show();
                }else {
                    articleStared=true;
                    operator.add(new Collection("ARTICLE",title,author,article));
                    star.setImageResource(R.drawable.stared);
                    Toast.makeText(mContext,"收藏成功",Toast.LENGTH_SHORT).show();
                }
            }
        });

        scrollBtn.setText("回到顶部");
        scrollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(View.FOCUS_UP);
            }
        });

    }
}
