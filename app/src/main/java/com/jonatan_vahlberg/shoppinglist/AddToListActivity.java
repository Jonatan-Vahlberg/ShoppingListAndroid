package com.jonatan_vahlberg.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.realm.Realm;

public class AddToListActivity extends AppCompatActivity {
    private EditText nameEdit,amountEdit,webEdit;
    private Button saveButton, returnButton;
    private Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_list);
        nameEdit = findViewById(R.id.nameEdit);
        amountEdit = findViewById(R.id.amountEdit);
        webEdit = findViewById(R.id.webEdit);

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_into_realm(nameEdit.getText().toString(),webEdit.getText().toString(),Integer.parseInt(amountEdit.getText().toString()));
                returnToMain();
            }
        });

        View view = findViewById(R.id.toolbarRelative);
        returnButton = view.findViewById(R.id.addButton);
        returnButton.setText("Return");
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMain();
            }
        });

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

    private void returnToMain(){
        finish();
        //Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        //startActivity(intent);
    }
}
