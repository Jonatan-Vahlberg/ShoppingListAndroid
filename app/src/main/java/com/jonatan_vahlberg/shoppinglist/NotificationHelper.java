package com.jonatan_vahlberg.shoppinglist;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {

    //SINGLETON PROPERTIES for creating notifications
    public static final String CHANNEL_ID = "channel_ID";
    public static final String CHANNEL_NAME = "Shopping List Reminder";
    public static final String  CHANNEL_DESC = "A user set shopping reminder of specific date";

    private NotificationManager mManager;


    //Constructor
    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
        }
    }

    //Targeted API VERSION OREO
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(CHANNEL_DESC);

        getManager().createNotificationChannel(channel);
    }

    //creates Manger if none exist
    public NotificationManager getManager() {
        if(mManager == null){
            mManager = (NotificationManager) getSystemService((Context.NOTIFICATION_SERVICE));
        }

        return  mManager;
    }

    /*Builder for Notifications
    * Builds a notification using standard properties
    * */
    public NotificationCompat.Builder getChannelNotification(String title){
        return new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle(getResources().getText(R.string.notification_title))
                .setContentText(getResources().getText(R.string.notification_message))
                .setSmallIcon(R.drawable.shopping);
    }
}
