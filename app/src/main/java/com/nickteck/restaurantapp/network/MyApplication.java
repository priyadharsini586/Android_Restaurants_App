package com.nickteck.restaurantapp.network;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.nickteck.restaurantapp.model.Constants;

/**
 * Created by admin on 3/7/2018.
 */




public class MyApplication extends Application {

    private static MyApplication mInstance;
    ConnectivityReceiver networkChangeReceiver;
    String isConnection = null;
    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;



        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            networkChangeReceiver = new ConnectivityReceiver()
            {
                @Override
                public void onReceive(Context context, Intent intent) {
                    super.onReceive(context, intent);

                    if (isConnection == null) {
                        Bundle b = intent.getExtras();
                        isConnection = b.getString(Constants.MESSAGE);
                    }
                }
            };

            Log.e("noguet","noguet");
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            intentFilter.addAction(Constants.BROADCAST);
            this.registerReceiver(networkChangeReceiver,
                    intentFilter);
        }
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }



}
