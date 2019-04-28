package com.jonatan_vahlberg.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ListsActivity extends AppCompatActivity {

    private Realm mRealm;
    private RealmResults<ShoppingList> mResults;

    private RealmChangeListener mChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
            RecyclerView rV = findViewById(R.id.recyclerListView);
            rV.getAdapter().notifyDataSetChanged();
            Log.d("onChange", "onChange: ");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        layoutSetup();
        realmSetup();
        //Init RecyclerView Adapter and View
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView rV = findViewById(R.id.recyclerListView);

        //Create New Adapter Based on RecyclerViewAdapter
        RecyclerViewListAdapter adapter = new RecyclerViewListAdapter(this,this.mResults);
        //Set Adapter
        rV.setAdapter(adapter);

        //Set layouManager
        rV.setLayoutManager(new LinearLayoutManager(this));
    }

    private void realmSetup(){

        //Get Realm file and Configuration Init in BaseApplication
        mRealm = Realm.getDefaultInstance();
        //Set Realm object Type for the RealmResults
        mResults = mRealm.where(ShoppingList.class).findAll();
        Log.d("onChange", "realmSetup: "+mResults.size());
        mRealm.addChangeListener(mChangeListener);
    }

    private void layoutSetup(){
        //Get Included ToolBar button
        View view = findViewById(R.id.toolbarRelative);
        TextView tV = view.findViewById(R.id.toolbar_relative_text);
        tV.setText("Shopping Lists");
        Button button = view.findViewById(R.id.addButton);
        button.setText("Add list");
        //Set Onclick Listener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddListActivity.class);
                startActivity(intent);
                //
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Get New Items or update old ones
        RecyclerView rV = findViewById(R.id.recyclerListView);
        rV.getAdapter().notifyDataSetChanged();
    }
}
