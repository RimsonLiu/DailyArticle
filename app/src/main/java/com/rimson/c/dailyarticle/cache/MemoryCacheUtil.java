package com.rimson.c.dailyarticle.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

class MemoryCacheUtil {
    private LruCache<String, Bitmap> memoryCache;

    MemoryCacheUtil() {
        //指定最大内存为手机内存的1/8
        long maxMemory = Runtime.getRuntime().maxMemory() / 8;
        memoryCache = new LruCache<String, Bitmap>((int) maxMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    Bitmap getBitmapFromMemory(String url) {
        return memoryCache.get(url);
    }

    void setBitmapToMemory(String url, Bitmap bitmap) {
        memoryCache.put(url, bitmap);
    }
}
