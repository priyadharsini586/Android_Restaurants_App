package com.nickteck.restaurantapp.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.nickteck.restaurantapp.Db.Database;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.network.ConnectivityReceiver;
import com.nickteck.restaurantapp.network.MyApplication;

public class SplashActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    boolean isNetworkConnected;
    private static int SPLASH_TIME_OUT = 3000;
    Database database;
    boolean data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        //code that displays the content in full screen mode
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        MyApplication.getInstance().setConnectivityListener(this);
        database = new Database(getApplicationContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                    Intent i = new Intent(SplashActivity.this,RatingActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                    finish();
                
                finish();
            }
        }, SPLASH_TIME_OUT);


    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if (isNetworkConnected != isConnected) {
            if (isConnected) {
                Toast.makeText(getApplicationContext(), "Network Connected", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
//                showSnackBar();
            }
        }

        isNetworkConnected = isConnected;

    }
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }
}
