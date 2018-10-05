package com.rimson.c.dailyarticle.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class VoiceBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getStringExtra("STATUS").equals("pause")){

        }
    }
}
