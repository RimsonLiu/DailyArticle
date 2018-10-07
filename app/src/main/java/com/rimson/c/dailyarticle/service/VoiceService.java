package com.rimson.c.dailyarticle.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rimson.c.dailyarticle.R;
import com.rimson.c.dailyarticle.activity.VoiceActivity;

import java.io.IOException;

public class VoiceService extends Service {
    public static String dataSource;

    public static MediaPlayer mediaPlayer;
    private VoiceBinder voiceBinder=new VoiceBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer=new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            mediaPlayer.reset();
            dataSource=VoiceActivity.downloadPath +VoiceActivity.fileName;
            mediaPlayer.setDataSource(dataSource);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
        }catch (IOException e){
            e.printStackTrace();
        }
        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        mediaPlayer=null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return voiceBinder;
    }

    public class VoiceBinder extends Binder{
        public void play(){
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
                //remoteViews.setImageViewResource(R.id.playOrPause,R.drawable.pause);
            }
        }

        public void pause(){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                //remoteViews.setImageViewResource(R.id.playOrPause,R.drawable.play);
            }
        }

        public void stop(){
            if (mediaPlayer!=null){
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }

        public int getProgress(){
            return mediaPlayer.getCurrentPosition()/1000;
        }

        public int getDuration(){
            return mediaPlayer.getDuration()/1000;
        }

        public boolean getStatus(){
            return mediaPlayer.isPlaying();
        }

        public void seekToPosition(int sec){
            mediaPlayer.seekTo(sec);
        }

    }

}
