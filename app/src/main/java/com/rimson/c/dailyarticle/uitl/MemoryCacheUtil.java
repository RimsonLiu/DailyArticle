package com.rimson.c.dailyarticle.uitl;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.io.File;

public class MemoryCacheUtil {
    private LruCache<String,Bitmap> memoryCache;

    public MemoryCacheUtil(){
        //指定最大内存为手机内存的1/8
        long maxMemory=Runtime.getRuntime().maxMemory()/8;
        memoryCache=new LruCache<String, Bitmap>((int)maxMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                int byteCount=value.getByteCount();
                return  byteCount;
            }
        };
    }

    public Bitmap getBitmapFromMemory(String url){
        Bitmap bitmap=memoryCache.get(url);
        return bitmap;
    }

    public void setBitmapToMemory(String url,Bitmap bitmap){
        memoryCache.put(url,bitmap);
    }
}
