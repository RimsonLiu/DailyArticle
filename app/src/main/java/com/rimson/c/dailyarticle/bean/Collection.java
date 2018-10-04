package com.rimson.c.dailyarticle.bean;

public class Collection {
    public String type;
    public String title;
    public String author;
    public String content;

    //type:ARTICLE，文章，content为正文
    //type:SOUND，声音，content为路径
    public Collection(String type,String title, String author, String content){
        this.type=type;
        this.title=title;
        this.author=author;
        this.content=content;
    }

}
