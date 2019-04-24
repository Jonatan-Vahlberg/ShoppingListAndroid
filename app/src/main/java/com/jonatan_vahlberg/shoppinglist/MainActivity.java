package com.jonatan_vahlberg.shoppinglist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ListObj> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBitMap();
        initRecyclerView();
        View view = findViewById(R.id.toolbarRelative);
        Button button = (Button) view.findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.add(new ListObj("Pear","https://cdn.pixabay.com/photo/2016/03/21/02/26/bartlett-pear-1269879_960_720.jpg"));
                RecyclerView rV = findViewById(R.id.recyclerView);
                rV.getAdapter().notifyDataSetChanged();
            }
        });


    }

    private void initBitMap(){
        mList.add(new ListObj("Apple","https://images.pexels.com/photos/39803/pexels-photo-39803.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260"));
        mList.add(new ListObj("Peach","https://images.pexels.com/photos/42218/food-fresh-fruit-isolated-42218.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260"));
        mList.add(new ListObj("Orange","https://images.pexels.com/photos/52533/orange-fruit-vitamins-healthy-eating-52533.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260"));
    }

    private void initRecyclerView(){
        RecyclerView rV = findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,this.mList);
        rV.setAdapter(adapter);
        rV.setLayoutManager(new LinearLayoutManager(this));
    }


}
