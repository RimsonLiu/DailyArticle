package com.rimson.c.dailyarticle.uitl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class LocalCacheUtil {
    private static final String CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dailyarticle/cache/";

    Bitmap getBitmapFromLocal(String url) {
        String fileName;
        try {
            fileName = MD5Util.encode(url);
            if (fileName != null) {
                File file = new File(CACHE_PATH, fileName);
                return BitmapFactory.decodeStream(new FileInputStream(file));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    void setBitmapToLocal(String url, Bitmap bitmap) {
        try {
            String fileName = MD5Util.encode(url);
            if (fileName != null) {
                File file = new File(CACHE_PATH, fileName);
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
