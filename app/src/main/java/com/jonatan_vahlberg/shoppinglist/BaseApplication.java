package com.jonatan_vahlberg.shoppinglist;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application {

    //public static final String CHANNEL_1_ID = "channel1";
    //public static final String CHANNEL_2_ID = "channel2";

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize Realm
        Realm.init(this);

        //Set Realm File Config
        RealmConfiguration  configuration = new RealmConfiguration.Builder().name("realm_v1_0_5").build();
        Realm.setDefaultConfiguration(configuration);


    }


}
