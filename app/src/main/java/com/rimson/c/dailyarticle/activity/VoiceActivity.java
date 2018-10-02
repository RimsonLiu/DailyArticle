package com.rimson.c.dailyarticle.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest.permission;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rimson.c.dailyarticle.R;
import com.rimson.c.dailyarticle.bean.Voice;
import com.rimson.c.dailyarticle.uitl.DownloadCompleteReceiver;
import com.rimson.c.dailyarticle.uitl.FileIsExists;
import com.rimson.c.dailyarticle.uitl.NetworkChangeReceiver;

public class VoiceActivity extends AppCompatActivity {
    private String title;
    private String author;
    private String imgURL;
    private String mp3URL;

    public static long downloadID;

    private NetworkChangeReceiver networkChangeReceiver;
    private DownloadCompleteReceiver downloadCompleteReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        initViews();
        checkNetwork();
        download();
        checkDownload();


    }



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
    }

    private void checkNetwork(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver=new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    private void download(){
        //动态获取写入权限
        if (ContextCompat.checkSelfPermission(VoiceActivity.this,permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(VoiceActivity.this,
                    new String[]{permission.WRITE_EXTERNAL_STORAGE},
                    23);
        }

        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(mp3URL));
        String downloadPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/dailyarticle/";
        String fileName=title+".mp3";
        Log.d("hehe",downloadPath+fileName);


        if (FileIsExists.fileIsExists(downloadPath+fileName)){
            //文件已存在
            Log.d("hehe","已存在");
        }else {
            Log.d("hehe","不存在");
            request.setDestinationInExternalPublicDir(downloadPath,fileName);
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
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
        unregisterReceiver(downloadCompleteReceiver);
    }
}
