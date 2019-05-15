package com.jonatan_vahlberg.shoppinglist;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ListsActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, DatePickerDialog.OnDateSetListener {

    Resources res;
    private Realm mRealm;
    private RealmResults<ShoppingList> mResults;

    private RealmChangeListener mChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object o) {

            recyclerView.getAdapter().notifyDataSetChanged();
            Log.d("onChange", "onChange: ");
        }
    };

    private RelativeLayout relativeLayout;
    private TextView title;
    private EditText newListEdit;
    private Button addButton;
    private ImageButton dateButton;
    private RecyclerView recyclerView;
    private RecyclerViewListAdapter adapter;

    private String dateString = "";
    private Calendar dateSet;
    private boolean dateActive;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        res = getResources();
        setContentView(R.layout.activity_lists);
        layoutSetup();
        realmSetup();
        //Init RecyclerView Adapter and View
        initRecyclerView();
        setupSwipeMethods();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Get New Items or update old ones
        RecyclerView rV = findViewById(R.id.recyclerListView);
        rV.getAdapter().notifyDataSetChanged();
    }


    //Setup Methods
    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerListView);

        //Create New Adapter Based on RecyclerViewAdapter
        RecyclerViewListAdapter adapter = new RecyclerViewListAdapter(this,this.mResults);
        //Set Adapter
        recyclerView.setAdapter(adapter);
        this.adapter = adapter;
        //Set layouManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        View view = findViewById(R.id.shoppingBar);
        relativeLayout = findViewById(R.id.relative_lists_layout);

        title = view.findViewById(R.id.toolbar_list_text);

        newListEdit = view.findViewById(R.id.list_name_edit);

        //Button Setups
        addButton = view.findViewById(R.id.list_btn_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newListEdit.getText().toString().equals("")){
                    Toast.makeText(ListsActivity.this, res.getText(R.string.toast_list_not_filled), Toast.LENGTH_SHORT).show();
                    return;
                }
                saveNewListToRealm();

            }
        });

        dateButton = view.findViewById(R.id.list_date_btn);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateActive){
                    dateActive = false;
                    dateButton.setImageResource(R.drawable.alarm_black);
                }
                else{
                    showDatePickerDialog();
                }

            }
        });
    }

    private  void showDatePickerDialog(){
        //create date picking fragment and save date from it
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    ///Realm Methods
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Switch layout for button
        dateButton.setImageResource(R.drawable.alarm);
        dateActive = true;

        //Tenerary operation for adding a 0 before integer thereby forcing it to be a string
        String dayString = (dayOfMonth < 10)? "0"+dayOfMonth : ""+dayOfMonth;
        String monthString = (1+month < 10)? "0"+month : ""+month;

        dateString = "" + dayString +"/"+ monthString +"/"+ year;
        dateSet = Calendar.getInstance();
        dateSet.set(Calendar.YEAR,year);
        dateSet.set(Calendar.MONTH,month);
        dateSet.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        //Alarm will be set for 15:00 o clock
        dateSet.set(Calendar.HOUR_OF_DAY,15);
        dateSet.set(Calendar.MINUTE,0);
        dateSet.set(Calendar.SECOND,0);
    }

    private void saveNewListToRealm(){

        //Long unique id for realm identification
        final long realmID = UUID.randomUUID().getMostSignificantBits();
        //first 8 digits of id used for creating a alarm id
        final int reminderID = Integer.parseInt(("" + realmID).substring(0, 8));

        //Realm Async Transaction
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //Realm Object creation and writing
                ShoppingList shoppingList = realm.createObject(ShoppingList.class, realmID);
                shoppingList.setAlarmId(reminderID);
                shoppingList.setName(newListEdit.getText().toString());
                if (!(dateString.equals(""))) {
                    shoppingList.setDate(dateString);
                }
                shoppingList.setListOfItems(new RealmList<ShoppingItem>());
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if(dateActive){

                    //If a date is chosen set alarm
                    setScheduledReminder(reminderID,false);

                }

                //reset all fields
                newListEdit.setText("");
                newListEdit.clearFocus();
                dateActive = false;
                dateButton.setImageResource(R.drawable.alarm_black);
                dateSet = null;
                dateString = "";
            }

        });
    }

    private void setScheduledReminder(int longKey, boolean update){

        /*Alarm manager takes a pending intent and sends
         it to Android OS wich safe keeps it even if app is off
          or disabled*/

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("title",newListEdit.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,longKey,intent,0);

        if(dateSet.before(Calendar.getInstance())){
            dateSet.add(Calendar.DATE,1);
        }
        //If the object is being updated clear previous intent
        if(update){
            alarmManager.cancel(pendingIntent);
        }

        //Send pending intent to OS
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, dateSet.getTimeInMillis(),pendingIntent);
    }


    ///Swipe-able Cells Methods
    public void setupSwipeMethods(){
        ItemTouchHelper.SimpleCallback touchHelperCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(touchHelperCallback).attachToRecyclerView(recyclerView);
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        //checks cells for correct types
        if(viewHolder instanceof RecyclerViewAdapter.ViewHolder || viewHolder instanceof  RecyclerViewListAdapter.ListViewHolder){
            ShoppingList item = mResults.get(position);
            String name = item.getName();

            //final ShoppingList deletedItem = item;
            final int deletedPosition = viewHolder.getAdapterPosition();

            adapter.itemToBeDeleted(deletedPosition);

            //Snackbar for undoing deletion
            Snackbar snackbar = Snackbar
                    .make(relativeLayout,name+" "+ res.getText(R.string.removed), Snackbar.LENGTH_LONG);

            //If clicked restore item
            snackbar.setAction(res.getText(R.string.undo), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(deletedPosition);
                }

            });

            //if item not restored delete fully
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
