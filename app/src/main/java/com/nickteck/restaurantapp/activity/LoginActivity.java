package com.nickteck.restaurantapp.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    CheckBox chkAdvanced;
    LinearLayout ldtEMail_id;
    Button btnSubmitLogin;
    EditText edtPhone, edtName, edtMailId,edtAddress;
    String strName, strMailId,strAddress,strTableId,strTableName,strSubName;
    boolean isNetworkConnected;
    RelativeLayout sclMainView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Get the bundle
        Bundle bundle = getIntent().getExtras();
         strTableName = bundle.getString("table_name");
        strTableId = bundle.getString("id");
        strSubName = bundle.getString("sub_name");


        chkAdvanced = (CheckBox) findViewById(R.id.chkAdvanced);
        chkAdvanced.setOnClickListener(this);

        ldtEMail_id = findViewById(R.id.ldtEMail_id);
//        ldtEMail_id.setVisibility(View.GONE);

        btnSubmitLogin = (Button) findViewById(R.id.btnSubmitLogin);
        btnSubmitLogin.setOnClickListener(this);

        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtName = (EditText) findViewById(R.id.edtName);
        edtMailId = (EditText) findViewById(R.id.edtMailId);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        sclMainView = (RelativeLayout) findViewById(R.id.sclMainView);
        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("string count", String.valueOf(s.length()));
                if (s.length() == 10)
                {
                    checkNumberLogin();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        progressBar =(ProgressBar) findViewById(R.id.progressLogin);
        progressBar.setVisibility(View.GONE);

        checkConnection();
        MyApplication.getInstance().setConnectivityListener(this);


    }


    private boolean isValidMobile(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() < 6 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;
                edtPhone.setError("Not Valid Number");
            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;
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

                if (isValidMobile(edtPhone.getText().toString()))
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
            if (edtAddress.getText().toString().isEmpty()) {
                strAddress = "";
            } else {
                strAddress = edtAddress.getText().toString();
            }

            try {
                jsonObject.put("phone", edtPhone.getText().toString());
                jsonObject.put("name", strName);
                jsonObject.put("email", strMailId);
                jsonObject.put("address", strAddress);
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
                            if (!strSubName.isEmpty())
                            {
                                strTableName = strTableName + "("+strSubName +")";
                            }
                            database.insertTable(strTableId, strTableName);
                            SharedPreferences settings = getApplicationContext().getSharedPreferences(Constants.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString(edtPhone.getText().toString(), Constants.MOBILE_NUMBER);
                            editor.apply();
                            Intent intent= new Intent(getApplicationContext(),MenuNavigationActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
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


    public void checkNumberLogin()
    {
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",edtPhone.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<LoginRequestAndResponse> loginRequestAndResponseCall = apiInterface.clientDetailsByNum(jsonObject);
        loginRequestAndResponseCall.enqueue(new Callback<LoginRequestAndResponse>() {
            @Override
            public void onResponse(Call<LoginRequestAndResponse> call, Response<LoginRequestAndResponse> response) {
                if (response.isSuccessful())
                {
                    LoginRequestAndResponse loginRequestAndResponse = response.body();
                    if (loginRequestAndResponse.getStatusCode().equals(Constants.Success))
                    {
                        edtAddress.setText(loginRequestAndResponse.getAddress());
                        edtName.setText(loginRequestAndResponse.getName());
                        edtMailId.setText(loginRequestAndResponse.getEmail());
                        progressBar.setVisibility(View.GONE);
                        AdditionalClass.hideKeyboard(LoginActivity.this);
                    }else
                    {
                        Toast.makeText(getApplicationContext(),loginRequestAndResponse.getStatusMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginRequestAndResponse> call, Throwable t) {

            }
        });
    }
}
