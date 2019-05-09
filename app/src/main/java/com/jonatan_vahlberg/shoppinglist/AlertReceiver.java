package com.jonatan_vahlberg.shoppinglist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String extra = "Shopping List";
        if(intent.hasExtra("title")){
            //extra = intent.getStringExtra("title");
            if(extra.equals("null")){
                extra = "Shopping List";
            }
        }
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(intent.getStringExtra("Shopping List"));
        notificationHelper.getManager().notify(1,nb.build());
    }
}
