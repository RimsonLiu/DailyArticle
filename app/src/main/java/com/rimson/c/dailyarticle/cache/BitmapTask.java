package com.rimson.c.dailyarticle.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.widget.ImageView;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitmapTask extends AsyncTask<Object, Void, Bitmap> {
    private WeakReference<ImageView> imageViewWeakReference;
    private String Url;
    private LocalCacheUtil localCacheUtil;
    private MemoryCacheUtil memoryCacheUtil;

    BitmapTask(ImageView imageView) {
        this.imageViewWeakReference = new WeakReference<>(imageView);
    }

    //子线程中进行后台的耗时操作
    @Override
    protected Bitmap doInBackground(Object[] objects) {
        Url = (String) objects[0];
        localCacheUtil = (LocalCacheUtil) objects[1];
        memoryCacheUtil = (MemoryCacheUtil) objects[2];
        return downloadBitmap(Url);
    }

    //耗时方法结束后，在主线程执行该方法
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            final ImageView imageView = imageViewWeakReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
            //从网络获取图片后，保存至本地和内存
            localCacheUtil.setBitmapToLocal(Url, bitmap);
            memoryCacheUtil.setBitmapToMemory(Url, bitmap);
        }
    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream(), null, options);

                assert bitmap != null;
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                //进行图片大小适配
                float scaleWidth = 4;
                float scaleHeight = 4;
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }
}
