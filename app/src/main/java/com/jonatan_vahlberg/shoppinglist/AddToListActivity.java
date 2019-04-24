package com.jonatan_vahlberg.shoppinglist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.realm.Realm;

public class AddToListActivity extends AppCompatActivity {

    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_list);
        realm = Realm.getDefaultInstance();
    }


    private void save_into_realm(final String name, final  String image, final int amount){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Product user = bgRealm.createObject(Product.class);
                user.setName(name);
                user.setAmount(amount);
                user.setImage(image);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                Log.d("Database","USER WAS ENTERED");
                //updateResults();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.d("Database","USER WAS NOT ENTERED");
            }
        });

    }
}
