package com.rimson.c.dailyarticle.activity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.Manifest.permission;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rimson.c.dailyarticle.R;
import com.rimson.c.dailyarticle.bean.Voice;
import com.rimson.c.dailyarticle.uitl.FileIsExists;
import com.rimson.c.dailyarticle.uitl.NetworkChangeReceiver;
import com.rimson.c.dailyarticle.uitl.DownloadCompleteReceiver;
import com.rimson.c.dailyarticle.uitl.CalculateTime;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class VoiceActivity extends AppCompatActivity {
    private String title;
    private String author;
    private String imgURL;
    private String mp3URL;
    private String downloadPath;
    private String fileName;

    private TextView currentTV;
    private TextView fullTV;
    private SeekBar seekBar;
    private ImageView imageBtn;

    private Timer timer;
    private MediaPlayer mediaPlayer;
    private boolean isPause=false;
    private boolean isSeekBarChanging=false;

    public static long downloadID;

    private NetworkChangeReceiver networkChangeReceiver;
    private DownloadCompleteReceiver downloadCompleteReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);

        initViews();
        checkNetwork();
        download();
        checkDownload();

    }

    //初始化布局
    private void initViews(){
        final Voice voice=getIntent().getParcelableExtra("VOICE");
        this.title=voice.title;
        this.author=voice.author;
        this.imgURL=voice.imgURL;
        this.mp3URL=voice.mp3URL;

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
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentTV.setText(CalculateTime.calculateTime(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekBarChanging=true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //getProgress()返回秒，seekTo()传入毫秒
                mediaPlayer.seekTo(seekBar.getProgress()*1000);
                currentTV.setText(CalculateTime.calculateTime(mediaPlayer.getCurrentPosition()/1000));
                isSeekBarChanging=false;
            }
        });

        imageBtn=(ImageView) findViewById(R.id.imageBtn);
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlayOrPause();
            }
        });

    }

    //初始化MediaPlayer
    private void initMediaPlayer(){
        mediaPlayer=new MediaPlayer();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(downloadPath+fileName);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
        }catch (IOException e){
            e.printStackTrace();
        }
        int fullTime=mediaPlayer.getDuration()/1000;
        int currentTime=mediaPlayer.getCurrentPosition()/1000;
        currentTV.setText(CalculateTime.calculateTime(currentTime));
        fullTV.setText(CalculateTime.calculateTime(fullTime));
        seekBar.setMax(fullTime);
    }

    //判断并执行播放或者暂停
    private void isPlayOrPause(){
        if (mediaPlayer!=null){
            if (!isPause){
                mediaPlayer.start();
                timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(!isSeekBarChanging){
                            try {
                                seekBar.setProgress(mediaPlayer.getCurrentPosition()/1000);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }
                },0,50);
                imageBtn.setImageResource(R.drawable.pause);
                isPause=true;
            } else{
                mediaPlayer.pause();
                imageBtn.setImageResource(R.drawable.play);
                isPause=false;
            }
        }else{
            Toast.makeText(getApplicationContext(),"播放失败",Toast.LENGTH_SHORT).show();
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
        if (ContextCompat.checkSelfPermission(VoiceActivity.this,permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(VoiceActivity.this,
                    new String[]{permission.WRITE_EXTERNAL_STORAGE},
                    23);
        }

        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(mp3URL));
        downloadPath=Environment.getExternalStorageDirectory().getPath()+"/dailyarticle/sound/";
        fileName=title+".mp3";

        if (FileIsExists.fileIsExists(downloadPath+fileName)){
            //文件已存在
            initMediaPlayer();
            isPlayOrPause();
        }else {
            request.setDestinationInExternalPublicDir("/dailyarticle/sound/",fileName);
            DownloadManager downloadManager=(DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            downloadID = downloadManager.enqueue(request);

            Toast.makeText(VoiceActivity.this,"开始下载音频",Toast.LENGTH_SHORT).show();
        }

    }

    private void checkDownload(){
        IntentFilter intentFilter=new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        downloadCompleteReceiver=new DownloadCompleteReceiver();
        registerReceiver(downloadCompleteReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer!=null){
            timer.cancel();
            timer.purge();
            timer=null;
        }
        if (mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
        isPause=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
        unregisterReceiver(downloadCompleteReceiver);

    }


}


