package com.jonatan_vahlberg.shoppinglist;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration  configuration = new RealmConfiguration.Builder().name("ShoppingRealm").build();
        Realm.setDefaultConfiguration(configuration);
    }
}
