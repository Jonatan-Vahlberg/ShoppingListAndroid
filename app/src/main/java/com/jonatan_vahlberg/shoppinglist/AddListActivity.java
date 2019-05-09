package com.jonatan_vahlberg.shoppinglist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class AddListActivity extends AppCompatActivity {

    private Realm realm;
    private EditText nameText;
    private DatePicker datePicker;
    private String  mDateString;
    private long id;
    private long newId = UUID.randomUUID().getMostSignificantBits();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);
        realm = Realm.getDefaultInstance();
        layoutSetup();
    }

    private void layoutSetup(){
        //Get Included ToolBar button
        View view = findViewById(R.id.toolbarRelative);
        TextView tV = view.findViewById(R.id.toolbar_relative_text);
        if(getIntent().hasExtra("Update")){
            tV.setText("Update List");
            id = getIntent().getLongExtra("Update",-666666666);
            Log.d("extra", "layoutSetup: " + id);
        }
        else{
            tV.setText("New List");
        }

        nameText = findViewById(R.id.add_list_name);
        datePicker = findViewById(R.id.date_date);

        Button button = view.findViewById(R.id.addButton);
        button.setText("Return");
        //Set Onclick Listener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //
            }
        });

        Button addButton = findViewById(R.id.list_add_btn);
        if(id > 0){
            addButton.setText("Update");
        }
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameText.getText().toString().equals("")){
                    Toast.makeText(AddListActivity.this,"Fill in Name Field",Toast.LENGTH_SHORT).show();
                    return;
                }
                getDateString();
                if(getIntent().hasExtra("Update")){
                    Long id = getIntent().getLongExtra("Update",0);
                    updateRealmObject(id);
                }
                else{
                    save_into_Realm();
                }
                setScheduledReminder(false,datePicker.getDayOfMonth(),datePicker.getMonth(),datePicker.getYear());
                finish();
            }

            private void getDateString() {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();

                String dateString = intToDateString(day,true);

                dateString += intToDateString(month,false);
                dateString += intToDateString(year,false);
                mDateString = dateString;
            }

            private String intToDateString(int num, boolean first) {
                if(first){
                    if(num <= 9) return  "0"+num;
                    else return ""+num;
                }
                if(num <= 9) return  "/0"+num;
                else return "/"+num;
            }
        });
    }

    private void setScheduledReminder(boolean update,int day, int month, int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,day);
        c.set(Calendar.HOUR_OF_DAY,10);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);

        int longKey = 0;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            String s = "";
            if(update){
                s = Long.toString(id);
            }
            else{
                s = Long.toString(newId);
            }
            String firstSevenDigits = s.substring(0,7);
            longKey = Integer.parseInt(firstSevenDigits);
        }
        else{
            longKey = (int) newId;
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("title",nameText.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,longKey,intent,0);

        if(c.before(Calendar.getInstance())){
            c.add(Calendar.DATE,1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),pendingIntent);

    }

    private void updateRealmObject(final Long id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
              ShoppingList shoppingList = realm.where(ShoppingList.class).equalTo("id",id).findFirst();
                if(shoppingList == null){
                    ShoppingList newShoppingList = realm.createObject(ShoppingList.class,id);
                    newShoppingList.setName(nameText.getText().toString());
                    newShoppingList.setDate(mDateString);
                    newShoppingList.setListOfItems(new RealmList<ShoppingItem>());
                }
                else{
                    shoppingList.setName(nameText.getText().toString());
                    shoppingList.setDate(mDateString);
                }
            }

        });
    }

    private void save_into_Realm() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                ShoppingList shoppingList = bgRealm.createObject(ShoppingList.class,newId);
                shoppingList.setName(nameText.getText().toString());
                shoppingList.setDate(mDateString);
                shoppingList.setListOfItems(new RealmList<ShoppingItem>());

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("onChange", "onSuccess: ");

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("onChange", "onError: "+ error);
            }
        });
    }


}
