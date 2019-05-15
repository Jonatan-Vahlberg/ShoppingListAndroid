package com.jonatan_vahlberg.shoppinglist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.net.URL;

import io.realm.Realm;
/**Activity used for adding new shoppingItem to Realm*/
public class AddToListActivity extends AppCompatActivity {

    //Private Properties
    private EditText nameEdit,amountEdit,webEdit;
    private Button saveButton, returnButton;
    private Realm realm;
    private long  mId;
    private Spinner spinner;
    private String[] spinnerValues = {"Pieces","Kg","L","Grams"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_list);


        //Getting Layout Elements
        nameEdit = findViewById(R.id.nameEdit);
        amountEdit = findViewById(R.id.amountEdit);
        webEdit = findViewById(R.id.webEdit);
        spinner = findViewById(R.id.amount_spinner);
        setSpinnerValue();
        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataIsPassable()) {
                    save_into_realm(nameEdit.getText().toString(), webEdit.getText().toString(), Integer.parseInt(amountEdit.getText().toString()));

                    returnToMain();
                }
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
        if(getIntent().hasExtra("id")){
            mId = getIntent().getLongExtra("id",0);
        }
    }


    private void save_into_realm(final String name, final  String image, final int amount){
        if (mId == 0){
            return;
        }
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                ShoppingItem shoppingItem = bgRealm.createObject(ShoppingItem.class);
                shoppingItem.setName(name);
                shoppingItem.setAmount(amount);
                //shoppingItem.setImage(image);
                shoppingItem.setChecked(false);
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

    }

    private void returnToMain(){
        finish();
        //Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        //startActivity(intent);
    }

    //does data entered conform to required forms
    private boolean dataIsPassable(){
        if(
                (nameEdit.getText().toString().equals("")) ||
                (amountEdit.getText().toString().equals(""))){

            Toast.makeText(this," Fields Needs to be filled in",Toast.LENGTH_SHORT).show();
            return  false;
        }
        else if(!(dataIsParseable(amountEdit.getText().toString()))){
            Toast.makeText(this,"Amount Needs to be a whole number",Toast.LENGTH_SHORT).show();
            return  false;
        }
        else if(webEdit.getText().toString().equals("")){
            return true;
        }
        else if(!(dataIsURL())){
            Toast.makeText(this,"Web Page image can't be created from Text Field",Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }

    }

    private boolean dataIsParseable(String  number){
        try{
            Integer.parseInt(number);

        }catch (Exception e){
            return false;
        }
        return true;

    }

    private boolean dataIsURL(){
        try{
            URL url = new URL(webEdit.getText().toString());
        }catch (Exception e){
            return  false;
        }
        return  true;
    }

    private void setSpinnerValue(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,spinnerValues);
        spinner.setAdapter(adapter);

    }
}
