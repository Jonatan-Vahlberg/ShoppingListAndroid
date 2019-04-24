package com.jonatan_vahlberg.shoppinglist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;

import io.realm.Realm;
/**Activity used for adding new product to Realm*/
public class AddToListActivity extends AppCompatActivity {

    //Private Properties
    private EditText nameEdit,amountEdit,webEdit;
    private Button saveButton, returnButton;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_list);

        //Getting Layout Elements
        nameEdit = findViewById(R.id.nameEdit);
        amountEdit = findViewById(R.id.amountEdit);
        webEdit = findViewById(R.id.webEdit);

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
    }


    private void save_into_realm(final String name, final  String image, final int amount){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Product product = bgRealm.createObject(Product.class);
                product.setName(name);
                product.setAmount(amount);
                product.setImage(image);
                product.setChecked(false);
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

    private boolean dataIsPassable(){
        if(
                (nameEdit.getText().toString().equals("")) ||
                (amountEdit.getText().toString().equals("")) ||
                (webEdit.getText().toString().equals(""))){

            Toast.makeText(this," Fields Needs to be filled in",Toast.LENGTH_SHORT).show();
            return  false;
        }
        else if(!(dataIsParseable(amountEdit.getText().toString()))){
            Toast.makeText(this,"Amount Needs to be a whole number",Toast.LENGTH_SHORT).show();
            return  false;
        }
        else if(!(dataIsURL())){
            Toast.makeText(this,"Web Page can't be created from Text Field",Toast.LENGTH_SHORT).show();
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
}
