package com.rimson.c.dailyarticle.cache;

import android.widget.ImageView;

class NetCacheUtil {
    private LocalCacheUtil localCacheUtil;
    private MemoryCacheUtil memoryCacheUtil;

    NetCacheUtil(LocalCacheUtil localUtil, MemoryCacheUtil memoryUtil) {
        this.localCacheUtil = localUtil;
        this.memoryCacheUtil = memoryUtil;
    }

    void getBitmapFromNet(ImageView imageView, String Url) {
        new BitmapTask(imageView).execute(Url, localCacheUtil, memoryCacheUtil);
    }

}
