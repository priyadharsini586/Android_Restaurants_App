package com.nickteck.restaurantapp.chat.rabbitmq_server;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.nickteck.restaurantapp.chat.rabbitmq_stomp.Client;
import com.nickteck.restaurantapp.chat.rabbitmq_stomp.Listener;
import com.nickteck.restaurantapp.model.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

/**
 * Created by admin on 3/7/2018.
 */

public class RabbitmqServer extends AsyncTask {

    int count = 0;

    public static Client client;

    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected Object doInBackground(Object[] params) {
        try {
            client = new Client(Constants.CHAT_SERVER_URL,61613, "restaurantServer", "restaurant");

            client.subscribe("/topic/resturantApp", new Listener() {
                @Override
                public void message(Map headers, String body) {


                    Log.e("receive message", body);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOException", String.valueOf(e));
        } catch (LoginException e) {
            e.printStackTrace();
            Log.e("IOException", String.valueOf(e));
        }
        return null;

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);


    }

    public  void sendMsg(String msg) {

        HashMap headers = new HashMap();
        headers.put("content-type", "text/plain");
        if (client!= null) {
            client.send("/topic/resturantApp", msg, headers);
            client.addErrorListener(new Listener() {

                @Override
                public void message(Map headers, String body) {

                    Log.e("send message", body);
                }
            });
        }else
        {
            Log.e("client connect", "client not connected");
        }
    }
}
