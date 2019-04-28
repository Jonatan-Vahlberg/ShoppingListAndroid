package com.jonatan_vahlberg.shoppinglist;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize Realm
        Realm.init(this);

        //Set Realm File Config
        RealmConfiguration  configuration = new RealmConfiguration.Builder().name("realm_v103").build();
        Realm.setDefaultConfiguration(configuration);
    }
}
