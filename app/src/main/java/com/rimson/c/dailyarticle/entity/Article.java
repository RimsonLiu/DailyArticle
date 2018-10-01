package com.rimson.c.dailyarticle.entity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Article {
    private String title;
    private String author;
    private String article;

    public Article(String title,String author,String article){
        this.title=title;
        this.author=author;
        this.article=article;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getArticle() {
        return article;
    }
}
