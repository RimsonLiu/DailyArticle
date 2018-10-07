package com.rimson.c.dailyarticle.uitl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetCacheUtil {
    private LocalCacheUtil localCacheUtil;
    private MemoryCacheUtil memoryCacheUtil;

    public NetCacheUtil(LocalCacheUtil localUtil,MemoryCacheUtil memoryUtil){
        this.localCacheUtil=localUtil;
        this.memoryCacheUtil=memoryUtil;
    }

    public void getBitmapFromNet(ImageView imageView,String Url){
        new BitmapTask().execute(imageView,Url);
    }

    class BitmapTask extends AsyncTask<Object,Void,Bitmap>{
        private ImageView imageView;
        private String Url;

        //子线程中进行后台的耗时操作
        @Override
        protected Bitmap doInBackground(Object[] objects) {
            imageView=(ImageView)objects[0];
            Url=(String)objects[1];
            return downloadBitmap(Url);
        }

        //耗时方法结束后，在主线程执行该方法

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap!=null){
                imageView.setImageBitmap(bitmap);
                //从网络获取图片后，保存至本地和内存
                localCacheUtil.setBitmapToLocal(Url,bitmap);
                memoryCacheUtil.setBitmapToMemory(Url,bitmap);
            }
        }


        private Bitmap downloadBitmap(String url){
            HttpURLConnection conn=null;
            try {
                conn=(HttpURLConnection)new URL(url).openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("GET");

                int responseCode=conn.getResponseCode();
                if (responseCode==200){
                    BitmapFactory.Options options=new BitmapFactory.Options();
                    //options.inSampleSize=2;//宽高压缩为二分之一
                    options.inPreferredConfig=Bitmap.Config.ARGB_4444;
                    Bitmap bitmap=BitmapFactory.decodeStream(conn.getInputStream(),null,options);

                    int width=bitmap.getWidth();
                    int height=bitmap.getHeight();
                    int newWidth=imageView.getWidth();
                    int newHeight=imageView.getHeight();
                    Log.d("hehe", String.valueOf(newHeight));

                    /*float scaleWidth=((float)newWidth)/width;
                    float scaleHeight=((float)newHeight)/height;*/
                    float scaleWidth=4;
                    float scaleHeight=4;
                    Matrix matrix=new Matrix();
                    matrix.postScale(scaleWidth,scaleHeight);
                    Bitmap newBitmap=Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
                    return newBitmap;
                }
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                assert conn != null;
                conn.disconnect();
            }
            return null;
        }
    }

}
