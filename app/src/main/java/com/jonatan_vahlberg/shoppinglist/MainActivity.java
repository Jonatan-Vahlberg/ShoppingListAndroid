package com.jonatan_vahlberg.shoppinglist;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import io.realm.Realm;
import io.realm.RealmChangeListener;


public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    static private Realm realm;
    private ShoppingList mList;
    private long mId;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private RelativeLayout relativeLayout;
    private View view;
    private Spinner spinner;
    private String[] spinnerValues;
    private EditText itemName, itemAmount;
    private Button addBtn;
    Resources res;


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
        res = getResources();
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_main);
        setupLayouts();
        toolbarListenerSetups();
        //Get Realm file and Configuration Init in BaseApplication
        setupRealm();

        //Init RecyclerView Adapter and View
        initRecyclerView();
        setupSwipeMethods();
        setSpinnerValues();


    }

    private void setupLayouts() {

        spinner = findViewById(R.id.item_spinner);
        relativeLayout = findViewById(R.id.relative_list_layout);
        view = findViewById(R.id.item_shoppingBar);
        itemName = view.findViewById(R.id.item_name);
        itemAmount = view.findViewById(R.id.item_amount);
        addBtn = view.findViewById(R.id.item_btn);
    }

    private void setupRealm(){
        realm = Realm.getDefaultInstance();
        if(getIntent().hasExtra("id")){
            mId = getIntent().getLongExtra("id",0);
            mList = realm.where(ShoppingList.class).equalTo("id",mId).findFirst();
            TextView textView = view.findViewById(R.id.item_toolbar_relative_text);
            textView.setText(mList.getName());
            mList.addChangeListener(mChangeListener);
            Log.d("Database", mId + "");
        }
    }

    private void toolbarListenerSetups() {
        itemAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                switch(actionId){

                    case EditorInfo.IME_ACTION_DONE:
                        if(!(v.getText().toString().equals(""))){
                            String name =  itemName.getText().toString();
                            int amount =  Integer.parseInt(itemAmount.getText().toString());
                            addNewItem(name,amount);
                        }
                        break;

                }

                return false;
            }
        });

        itemName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                itemAmount.requestFocus();
                return true;
            }});


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkDataValidity()){
                    String name =  itemName.getText().toString();
                    int amount =  Integer.parseInt(itemAmount.getText().toString());
                    addNewItem(name,amount);

                }

            }
        });
    }

    private void clearFields(){
        itemName.setText("");
        itemName.requestFocus();
        itemAmount.setText("");
    }

    private boolean checkDataValidity() {
        if(itemName.getText().toString().equals("") || itemAmount.getText().toString().equals("")){
            Toast.makeText(this,res.getText(R.string.toast_item_not_filled), Toast.LENGTH_SHORT).show();
            return false;
        }
        try{
            Integer.parseInt(itemAmount.getText().toString());
        }catch (Exception e){
            return false;
        }
        return true;
    }

    private void addNewItem(final String name, final int amount) {

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    ShoppingItem shoppingItem = bgRealm.createObject(ShoppingItem.class);
                    shoppingItem.setName(name);
                    shoppingItem.setAmount(amount);
                    shoppingItem.setChecked(false);
                    shoppingItem.setToBeDeleted(false);
                    shoppingItem.setAmountType(spinner.getSelectedItem().toString());
                    bgRealm.where(ShoppingList.class).equalTo("id",mId).findFirst().getListOfItems().add(shoppingItem);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    // Transaction was a success.
                    Log.d("Database","Item WAS ENTERED");
                    //updateResults();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    // Transaction failed and was automatically canceled.
                    Log.d("Database","Item WAS NOT ENTERED");
                }
            });

            clearFields();
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
    public void setupSwipeMethods(){
        ItemTouchHelper.SimpleCallback touchHelperCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(touchHelperCallback).attachToRecyclerView(recyclerView);
    }

    private void setSpinnerValues(){
        spinnerValues = getResources().getStringArray(R.array.postfix);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_custom_item,spinnerValues);
        spinner.setAdapter(adapter);

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
            final int deletedPosition = viewHolder.getAdapterPosition();

            adapter.itemToBeDeleted(deletedPosition);

            Snackbar snackbar = Snackbar
                    .make(relativeLayout,name+" "+res.getText(R.string.removed_item), Snackbar.LENGTH_LONG);
            snackbar.setAction(res.getText(R.string.undo), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(deletedPosition);
                }

            });
            snackbar.addCallback(new Snackbar.Callback(){

                @Override
                public void onDismissed(Snackbar snackbar, int event){
                    adapter.willDeleteItem(deletedPosition);
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
