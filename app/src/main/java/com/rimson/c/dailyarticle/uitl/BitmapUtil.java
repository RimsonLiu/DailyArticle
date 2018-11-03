package com.rimson.c.dailyarticle.uitl;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.rimson.c.dailyarticle.R;

public class BitmapUtil {
    private NetCacheUtil netCacheUtil;
    private LocalCacheUtil localCacheUtil;
    private MemoryCacheUtil memoryCacheUtil;

    public BitmapUtil() {
        localCacheUtil = new LocalCacheUtil();
        memoryCacheUtil = new MemoryCacheUtil();
        netCacheUtil = new NetCacheUtil(localCacheUtil, memoryCacheUtil);
    }

    public void display(ImageView imageView, String url) {
        imageView.setImageResource(R.mipmap.ic_launcher_round);
        Bitmap bitmap;

        //内存缓存
        bitmap = memoryCacheUtil.getBitmapFromMemory(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }

        //本地缓存
        bitmap = localCacheUtil.getBitmapFromLocal(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }

        //网络缓存
        netCacheUtil.getBitmapFromNet(imageView, url);
    }

}
