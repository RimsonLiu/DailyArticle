package com.rimson.c.dailyarticle.activity;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.Manifest.permission;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rimson.c.dailyarticle.R;
import com.rimson.c.dailyarticle.bean.Collection;
import com.rimson.c.dailyarticle.bean.Voice;
import com.rimson.c.dailyarticle.db.Operator;
import com.rimson.c.dailyarticle.service.VoiceService;
import com.rimson.c.dailyarticle.uitl.FileIsExists;
import com.rimson.c.dailyarticle.broadcast.NetworkChangeReceiver;
import com.rimson.c.dailyarticle.broadcast.DownloadCompleteReceiver;
import com.rimson.c.dailyarticle.uitl.CalculateTime;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class VoiceActivity extends AppCompatActivity {
    private String title;
    private String author;
    private String imgURL;
    private String mp3URL;
    public static String downloadPath;
    public static String fileName;
    private boolean voiceStared;

    private TextView currentTV;
    private TextView fullTV;
    private SeekBar seekBar;
    private ImageView voiceStar;
    private ImageView imageBtn;

    private Timer timer;
    private boolean isPause=false;
    private boolean isFirst=true;

    public static long downloadID;

    private NetworkChangeReceiver networkChangeReceiver;
    private DownloadCompleteReceiver downloadCompleteReceiver;

    public static Notification notification;
    private static NotificationManager notificationManager;
    private static Context mContext;

    private Operator operator;
    private VoiceService.VoiceBinder voiceBinder;

    static String PLAYER_TAG;

    private Handler mHandler=new Handler();
    private Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(voiceBinder.getProgress());
            currentTV.setText(CalculateTime.calculateTime(voiceBinder.getProgress()));
            mHandler.post(mRunnable);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_voice);
        mContext=this;
        PLAYER_TAG=getPackageName();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initViews();
        initNotification();
        checkNetwork();
        if (isFirst){
            download();
            checkDownload();
            isFirst=false;
        }

    }

    //初始化布局
    private void initViews(){
        final Voice voice=getIntent().getParcelableExtra("VOICE");
        this.title=voice.title;
        this.author=voice.author;
        this.imgURL=voice.imgURL;
        this.mp3URL=voice.mp3URL;

        operator=new Operator(mContext);
        voiceStared=operator.dataExists(new Collection("VOICE",title,author,null));

        setTitle("播放");

        ImageView imageView=(ImageView)findViewById(R.id.voice_img);
        TextView titleTV=(TextView)findViewById(R.id.voice_title);
        TextView authorTV=(TextView)findViewById(R.id.voice_author);

        Glide
                .with(getApplicationContext())
                .load(imgURL)
                .apply(new RequestOptions()
                .fitCenter())
                .into(imageView);

        titleTV.setText(title);
        authorTV.setText(author);

        currentTV=(TextView)findViewById(R.id.current_time);
        fullTV=(TextView)findViewById(R.id.full_time);
        seekBar=(SeekBar)findViewById(R.id.seekBar);


        voiceStar=(ImageView)findViewById(R.id.voice_star);
        if (voiceStared){
            voiceStar.setImageResource(R.drawable.stared);
        }
        voiceStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voiceStared){
                    voiceStared=false;
                    operator.delete(new Collection("VOICE",title,author,null));
                    voiceStar.setImageResource(R.drawable.star);
                    Toast.makeText(mContext,"取消收藏",Toast.LENGTH_SHORT).show();
                }else {
                    voiceStared=true;
                    operator.add(new Collection("VOICE",title,author,downloadPath+fileName));
                    voiceStar.setImageResource(R.drawable.stared);
                    Toast.makeText(mContext,"收藏成功",Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageBtn=(ImageView) findViewById(R.id.voice_pause);
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //isPlayOrPause();
                if (isPause){
                    voiceBinder.play();
                    imageBtn.setImageResource(R.drawable.pause);
                    isPause=false;
                }else {
                    voiceBinder.pause();
                    imageBtn.setImageResource(R.drawable.play);
                    isPause=true;
                }

            }
        });


    }

    //初始化通知栏
    private void initNotification(){
        notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(this,"voice");
        notification=new Notification();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel=new NotificationChannel("voice","通知栏",NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        RemoteViews remoteViews=new RemoteViews(getPackageName(),R.layout.notification);
        remoteViews.setTextViewText(R.id.ntfTitle,title);
        remoteViews.setTextViewText(R.id.ntfAuthor,author);
        remoteViews.setImageViewResource(R.id.playOrPause,R.drawable.pause);
        remoteViews.setImageViewResource(R.id.close,R.drawable.close);

        Intent intentPause=new Intent(PLAYER_TAG);
        intentPause.putExtra("STATUS","pause");
        PendingIntent pIntentPause=PendingIntent.getBroadcast(this,2,intentPause,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.playOrPause,pIntentPause);

        Intent notificationIntent=new Intent(this,VoiceActivity.class);
        PendingIntent intent=PendingIntent.getActivity(this,0,notificationIntent,0);
        mBuilder.setContent(remoteViews)
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(intent);

        notification=mBuilder.build();
        notification.flags= Notification.FLAG_NO_CLEAR;//滑动或点击时不被清除
        notificationManager.notify(PLAYER_TAG,111,notification);
    }

    //更新通知栏状态
    private void updateNotification(boolean isPause){
        if (!isPause){
            
        }
    }

    //判断网络状况
    private void checkNetwork(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver=new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    //判断音频文件是否存在，存在则播放，不存在则下载
    private void download(){
        //动态获取写入权限
        if (ContextCompat.checkSelfPermission(mContext,permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(VoiceActivity.this,
                    new String[]{permission.WRITE_EXTERNAL_STORAGE},
                    23);
        }

        downloadPath=Environment.getExternalStorageDirectory().getPath()+"/dailyarticle/sound/";
        fileName=title+".mp3";
        //文件已存在
        if (FileIsExists.fileIsExists(downloadPath+fileName)){
            Intent intent=new Intent(VoiceActivity.this,VoiceService.class);
            bindService(intent,voiceServiceConnection,BIND_AUTO_CREATE);
            //重新进入正在播放的声音，不会从头开始
            if (!(downloadPath+fileName).equals(VoiceService.dataSource)){
                startService(intent);
            }
        }else {
            //不存在则下载
            imageBtn.setImageResource(R.drawable.play);
            DownloadManager.Request request=new DownloadManager.Request(Uri.parse(mp3URL));
            request.setDestinationInExternalPublicDir("/dailyarticle/sound/",fileName);
            DownloadManager downloadManager=(DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            downloadID = downloadManager.enqueue(request);

            Toast.makeText(mContext,"开始下载音频",Toast.LENGTH_SHORT).show();
        }

    }

    //监听下载完成
    private void checkDownload(){
        IntentFilter intentFilter=new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        downloadCompleteReceiver=new DownloadCompleteReceiver();
        registerReceiver(downloadCompleteReceiver,intentFilter);
    }

    //绑定服务
    private ServiceConnection voiceServiceConnection=new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            voiceBinder=(VoiceService.VoiceBinder)service;
            seekBar.setMax(voiceBinder.getDuration());
            fullTV.setText(CalculateTime.calculateTime(voiceBinder.getDuration()));

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser){
                        currentTV.setText(CalculateTime.calculateTime(seekBar.getProgress()));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    //SeekBar.getProgress()返回秒，MediaPlayer.seekTo()传入毫秒
                    voiceBinder.seekToPosition(seekBar.getProgress()*1000);
                    currentTV.setText(CalculateTime.calculateTime(voiceBinder.getProgress()));
                }
            });
            mHandler.post(mRunnable);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
        unregisterReceiver(downloadCompleteReceiver);

        if (timer!=null){
            timer.cancel();
            timer.purge();
            timer=null;
        }

        if (notificationManager!=null){
            notificationManager.cancel(PLAYER_TAG,111);
        }
        isPause= true;
    }


}


