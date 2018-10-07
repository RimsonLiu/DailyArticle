package com.rimson.c.dailyarticle.broadcast;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.rimson.c.dailyarticle.activity.VoiceActivity;

import static com.rimson.c.dailyarticle.activity.VoiceActivity.downloadID;

public class DownloadCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long downloadCompleteId=intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
        if (downloadCompleteId==downloadID){
            Toast.makeText(context,"下载完成",Toast.LENGTH_SHORT).show();
        }
    }


}