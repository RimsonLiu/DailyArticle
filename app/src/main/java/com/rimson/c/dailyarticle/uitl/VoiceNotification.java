package com.rimson.c.dailyarticle.uitl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.rimson.c.dailyarticle.R;

public class VoiceNotification extends Notification {
    private final int NOTIFICATION_ID=10000;
    private NotificationManager manager;
    private Builder builder;
    private Context context;
    private RemoteViews remoteViews;
    private final int REQUEST_CODE=30000;

    private final String VOICE_NOTIFICATION_ACTION_PLAY = "voicenotificaion.To.PLAY";
    private final String VOICE_NOTIFICATION_ACTION_CLOSE = "voicenotificaion.To.CLOSE";

    private Intent play,close;
    private PendingIntent pendingIntent;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setManager(NotificationManager manager) {
        this.manager = manager;
    }

    private VoiceNotification(){
        remoteViews=new RemoteViews("com.rimson.c.dailyarticle", R.layout.notification);
        builder=new Builder(context);

        play=new Intent();
        play.setAction(VOICE_NOTIFICATION_ACTION_PLAY);
    }


}
