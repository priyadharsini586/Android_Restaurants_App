package com.nickteck.restaurantapp.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.nickteck.restaurantapp.Db.Database;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.additional_class.AdditionalClass;
import com.nickteck.restaurantapp.api.ApiClient;
import com.nickteck.restaurantapp.api.ApiInterface;
import com.nickteck.restaurantapp.model.Constants;
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
    RelativeLayout sclMainView;
    ProgressBar progressBar;

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

        sclMainView = (RelativeLayout) findViewById(R.id.sclMainView);

        progressBar =(ProgressBar) findViewById(R.id.progressLogin);
        progressBar.setVisibility(View.GONE);

        checkConnection();
        MyApplication.getInstance().setConnectivityListener(this);


    }



    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            isNetworkConnected = false;
            Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
        }else if (isConnected)
        {
            isNetworkConnected = true;
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.chkAdvanced:
                checkboxAdvanced();
                break;

            case R.id.btnSubmitLogin:

                if (!edtPhone.getText().toString().isEmpty())
                    checkLogin();
                else
                    Toast.makeText(getApplicationContext(),"Please Enter your mobile number",Toast.LENGTH_LONG).show();

                break;
        }
    }

    private void checkLogin() {

        if (isNetworkConnected) {
            progressBar.setVisibility(View.VISIBLE);
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
                        if (loginRequestAndResponse.getCustomer_id() != null) {
                            Database database = new Database(getApplicationContext());
                            database.insertCustomerTable("1", loginRequestAndResponse.customer_id);
                            Intent intent= new Intent(getApplicationContext(),MenuNavigationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginRequestAndResponse> call, Throwable t) {

                }
            });

        }else
        {
            AdditionalClass.showSnackBar(LoginActivity.this);
        }
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
                AdditionalClass.showSnackBar(LoginActivity.this);

            }
        }
        isNetworkConnected = isConnected;

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


}
