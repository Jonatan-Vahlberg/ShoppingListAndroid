package com.jonatan_vahlberg.shoppinglist;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import io.realm.Realm;
import io.realm.RealmChangeListener;

import static com.jonatan_vahlberg.shoppinglist.BaseApplication.CHANNEL_1_ID;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    static Realm realm;
    private ShoppingList mList;
    private long mId;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private RelativeLayout relativeLayout;


    private RealmChangeListener mChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object o) {

            recyclerView.getAdapter().notifyDataSetChanged();
            Log.d("onChange", "onChange: ");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Realm file and Configuration Init in BaseApplication
        realm = Realm.getDefaultInstance();

        relativeLayout = findViewById(R.id.relative_list_layout);
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
            }
        });



        //Init RecyclerView Adapter and View
        initRecyclerView();
        setupSwipemethods();
    }

    private void initRecyclerView(){

        recyclerView = findViewById(R.id.recyclerView);

        //Create New Adapter Based on RecyclerViewAdapter
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,this.mList.getListOfItems());
        //Set Adapter
        recyclerView.setAdapter(adapter);

        //Set layouManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.adapter =  adapter;
    }
    public void setupSwipemethods(){
        ItemTouchHelper.SimpleCallback touchHelperCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(touchHelperCallback).attachToRecyclerView(recyclerView);
    }



    //When Returning To MainActivity
    @Override
    protected void onResume() {
        super.onResume();
        //Get New Items or update old ones
        RecyclerView rV = findViewById(R.id.recyclerView);
        rV.getAdapter().notifyDataSetChanged();
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name;
        }
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof RecyclerViewAdapter.ViewHolder){
            ShoppingItem item = mList.getListOfItems().get(position);
            String name = item.getName();

            final ShoppingItem deletedItem = item;
            final int deletedPostion = viewHolder.getAdapterPosition();

            adapter.itemToBeDeleted(deletedPostion);

            Snackbar snackbar = Snackbar
                    .make(relativeLayout,name+" removed from list", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(deletedPostion);
                }

            });
            snackbar.addCallback(new Snackbar.Callback(){

                @Override
                public void onDismissed(Snackbar snackbar, int event){
                    adapter.willDeleteItem(deletedPostion);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
