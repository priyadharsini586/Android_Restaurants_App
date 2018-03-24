package com.nickteck.restaurantapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.nickteck.restaurantapp.Adapter.GridAdapter;
import com.nickteck.restaurantapp.R;

public class ItemActivity extends AppCompatActivity {
    GridView gridView;
    int img[]={R.drawable.cook2,R.drawable.cook1,R.drawable.cook3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        gridView=(GridView)findViewById(R.id.simpleGridView);
        GridAdapter gridAdapter=new GridAdapter(this,img);
        gridView.setAdapter(gridAdapter);


    }
}
