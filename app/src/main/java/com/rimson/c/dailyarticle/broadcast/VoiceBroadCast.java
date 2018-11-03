package com.rimson.c.dailyarticle.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rimson.c.dailyarticle.activity.VoiceActivity;

import static com.rimson.c.dailyarticle.service.VoiceService.mediaPlayer;

public class VoiceBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals("play_or_pause")) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                VoiceActivity.updateNotification(true);
            } else {
                mediaPlayer.start();
                VoiceActivity.updateNotification(false);
            }
        } else if (action != null && action.equals("close")) {
            mediaPlayer.pause();
            VoiceActivity.clearNotification();
        }

    }
}
