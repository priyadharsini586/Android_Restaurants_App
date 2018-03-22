package com.nickteck.restaurantapp.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.additional_class.AdditionalClass;
import com.nickteck.restaurantapp.api.ApiClient;
import com.nickteck.restaurantapp.api.ApiInterface;
import com.nickteck.restaurantapp.model.LoginRequestAndResponse;
import com.nickteck.restaurantapp.network.ConnectivityReceiver;
import com.nickteck.restaurantapp.network.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    CheckBox chkAdvanced;
    LinearLayout ldtEMail_id;
    Button btnSubmitLogin;
    EditText edtPhone, edtName, edtMailId;
    String strName, strMailId;
    boolean isNetworkConnected;
    ScrollView sclMainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        chkAdvanced = (CheckBox) findViewById(R.id.chkAdvanced);
        chkAdvanced.setOnClickListener(this);

        ldtEMail_id = findViewById(R.id.ldtEMail_id);
        ldtEMail_id.setVisibility(View.GONE);

        btnSubmitLogin = (Button) findViewById(R.id.btnSubmitLogin);
        btnSubmitLogin.setOnClickListener(this);

        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtName = (EditText) findViewById(R.id.edtName);
        edtMailId = (EditText) findViewById(R.id.edtMailId);

        sclMainView = (ScrollView) findViewById(R.id.sclMainView);

        checkConnection();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.chkAdvanced:
                checkboxAdvanced();
                break;

            case R.id.btnSubmitLogin:
                if (isNetworkConnected)
                    checkLogin();
                else
                    showSnackBar();
                break;
        }
    }

    private void checkLogin() {


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JSONObject jsonObject = new JSONObject();
        if (edtName.getText().toString().isEmpty()) {
            strName = "";
        } else {
            strName = edtName.getText().toString();
        }
        if (edtMailId.getText().toString().isEmpty()) {
            strMailId = "";
        } else {
            strMailId = edtMailId.getText().toString();
        }
        try {
            jsonObject.put("phone", edtPhone.getText().toString());
            jsonObject.put("name", strName);
            jsonObject.put("email", strMailId);
            jsonObject.put("dob", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<LoginRequestAndResponse> loginRequestAndResponseCall = apiInterface.getLoginResponse(jsonObject);
        loginRequestAndResponseCall.enqueue(new Callback<LoginRequestAndResponse>() {
            @Override
            public void onResponse(Call<LoginRequestAndResponse> call, Response<LoginRequestAndResponse> response) {
                if (response.isSuccessful()) {
                    LoginRequestAndResponse loginRequestAndResponse = response.body();
                    Log.e("response", String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<LoginRequestAndResponse> call, Throwable t) {

            }
        });


    }

    public void checkboxAdvanced() {
        if (chkAdvanced.isChecked()) {
            ldtEMail_id.setVisibility(View.VISIBLE);
        } else {
            ldtEMail_id.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if (isNetworkConnected != isConnected) {
            if (isConnected) {
                Toast.makeText(getApplicationContext(), "Network Connected", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
                showSnackBar();
            }
        }

        isNetworkConnected = isConnected;

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public  void showSnackBar()
    {
        TSnackbar snackbar = TSnackbar
                .make(findViewById(android.R.id.content), "Network Not Connected", TSnackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("Action Button", "onClick triggered");
                    }
                });
        snackbar.setActionTextColor(Color.parseColor("#00628f"));

//        snackbar.addIcon(R.mipmap.ic_core, 200); <<-- replace me!
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor("#f48220"));
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        snackbar.setDuration(60000);

        snackbar.show();

    }
}
