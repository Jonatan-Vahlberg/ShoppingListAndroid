package com.jonatan_vahlberg.shoppinglist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    Realm realm;
    RealmResults<Product> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        View view = findViewById(R.id.toolbarRelative);
        Button button = view.findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RecyclerView rV = findViewById(R.id.recyclerView);
                rV.getAdapter().notifyDataSetChanged();
            }
        });
        realm = Realm.getDefaultInstance();
        mList = realm.where(Product.class).findAll();
        initRecyclerView();


    }

    private void initRecyclerView(){
        RecyclerView rV = findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,this.mList);
        rV.setAdapter(adapter);
        rV.setLayoutManager(new LinearLayoutManager(this));
    }


}
