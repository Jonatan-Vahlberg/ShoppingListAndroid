package com.jonatan_vahlberg.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    static Realm realm;
    private ShoppingList mList;
    private long mId;


    private RealmChangeListener mChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
            RecyclerView rV = findViewById(R.id.recyclerView);
            rV.getAdapter().notifyDataSetChanged();
            Log.d("onChange", "onChange: ");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get Realm file and Configuration Init in BaseApplication
        realm = Realm.getDefaultInstance();


        //Get Included ToolBar button
        View view = findViewById(R.id.toolbarRelative);
        if(getIntent().hasExtra("id")){
            mId = getIntent().getLongExtra("id",0);
            mList = realm.where(ShoppingList.class).equalTo("id",mId).findFirst();
            TextView textView = view.findViewById(R.id.toolbar_relative_text);
            textView.setText(mList.getName());
            mList.addChangeListener(mChangeListener);
            Log.d("Database", mId + "");
        }
        Button button = view.findViewById(R.id.addButton);

        //Set Onclick Listener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddToListActivity.class);
                intent.putExtra("id",mId);
                startActivity(intent);
                //
            }
        });



        //Init RecyclerView Adapter and View
        initRecyclerView();
    }

    private void initRecyclerView(){

        RecyclerView rV = findViewById(R.id.recyclerView);

        //Create New Adapter Based on RecyclerViewAdapter
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,this.mList.getListOfItems());
        //Set Adapter
        rV.setAdapter(adapter);

        //Set layouManager
        rV.setLayoutManager(new LinearLayoutManager(this));
    }


    //When Returning To MainActivity
    @Override
    protected void onResume() {
        super.onResume();
        //Get New Items or update old ones
        RecyclerView rV = findViewById(R.id.recyclerView);
        rV.getAdapter().notifyDataSetChanged();
    }


}
