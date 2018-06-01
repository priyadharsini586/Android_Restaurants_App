package com.nickteck.restaurantapp.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.nickteck.restaurantapp.Adapter.TableAdapter;
import com.nickteck.restaurantapp.Db.Database;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.additional_class.AdditionalClass;
import com.nickteck.restaurantapp.api.ApiClient;
import com.nickteck.restaurantapp.api.ApiInterface;
import com.nickteck.restaurantapp.model.AddWhislist;
import com.nickteck.restaurantapp.model.Constants;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.nickteck.restaurantapp.model.LoginRequestAndResponse;
import com.nickteck.restaurantapp.model.TableModel;
import com.nickteck.restaurantapp.network.ConnectivityReceiver;
import com.nickteck.restaurantapp.network.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener  {
    TableAdapter tableAdapter;
    ApiInterface apiInterface;
    MaterialSpinner spin;
    Database database;
    Button button;
    TableModel.list table;
    private ArrayList<String> tableList = new ArrayList<>();
    private ArrayList<TableModel.list> tableItem = new ArrayList<>();
    CheckBox chkA,chkB,chkC,chkD,chkE,chkF;
    String checkStr = "";
    ProgressBar progressTable;
    boolean isNetworkConnected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        // checking internet connection is exist or not
        MyApplication.getInstance().setConnectivityListener(this);
        if (AdditionalClass.isNetworkAvailable(this)) {
            isNetworkConnected = true;
        }else {
            isNetworkConnected = false;
        }
        database = new Database(getApplicationContext());
        spin = (MaterialSpinner) findViewById(R.id.tableSpinner);
        //button=(Button)findViewById(R.id.button);
        progressTable = (ProgressBar) findViewById(R.id.progressTable);
        progressTable.setVisibility(View.VISIBLE);
        getServerIp();
        getTabledata();

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkConnected){
                    if (table != null) {
                        Intent i = new Intent(TableActivity.this, LoginActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id", table.getId());
                        bundle.putString("table_name", table.getName());
                        bundle.putString("sub_name", checkStr);
                        i.putExtras(bundle);
                        startActivity(i);
                        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                        finish();
                    }
                }else {
                    AdditionalClass.showSnackBar(TableActivity.this);
                }

            }
        });


        spin.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
//                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                if (position != 0)
                    table = tableItem.get(position);
                else
                    Toast.makeText(getApplicationContext(), "Please select valid Table", Toast.LENGTH_LONG).show();

            }
        });

        chkA = (CheckBox) findViewById(R.id.chkA);
        chkA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    checkStr = "A";
            }
        });
        chkB = (CheckBox) findViewById(R.id.chkB);
        chkB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    checkStr = "B";
            }
        });
        chkC = (CheckBox) findViewById(R.id.chkC);
        chkC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    checkStr = "C";
                else
                    checkStr = "";
            }
        });
        chkD = (CheckBox) findViewById(R.id.chkD);
        chkD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    checkStr = "D";
                else
                    checkStr = "";
            }
        });
        chkE = (CheckBox) findViewById(R.id.chkE);
        chkE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    checkStr = "E";
                else
                    checkStr = "";
            }
        });
        chkF = (CheckBox) findViewById(R.id.chkF);
        chkF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    checkStr = "F";
                else
                    checkStr = "";
            }
        });
    }

    private void getServerIp() {


        apiInterface = ApiClient.getClient().create(ApiInterface.class);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("act","get");
                jsonObject.put("current_ip", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Call<LoginRequestAndResponse> customerDetailsCall = apiInterface.getIp(jsonObject);
            customerDetailsCall.enqueue(new Callback<LoginRequestAndResponse>() {
                @Override
                public void onResponse(Call<LoginRequestAndResponse> call, Response<LoginRequestAndResponse> response) {
                    if (response.isSuccessful())
                    {
                        LoginRequestAndResponse loginRequestAndResponse = response.body();
                        if (loginRequestAndResponse.getStatusCode().equals(Constants.Success))
                        {
                            Constants.CHAT_SERVER_URL = loginRequestAndResponse.getCurrent_ip();
                        }

                    }
                }

                @Override
                public void onFailure(Call<LoginRequestAndResponse> call, Throwable throwable) {

                }
            });

    }


    public void getTabledata() {

            apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Call<TableModel> getCatageoryList = apiInterface.getTableData();
            getCatageoryList.enqueue(new Callback<TableModel>() {
                @Override
                public void onResponse(Call<TableModel> call, Response<TableModel> response) {
                    if (response.isSuccessful())
                    {
                        TableModel tableModel = response.body();
                        progressTable.setVisibility(View.GONE);
                        if (tableModel.getStatus_code().equals(Constants.Success))
                        {
                            tableList = new ArrayList<String>();
                            tableList.add("Table List");
                            tableItem.add(null);
                            List<TableModel.list> lsttbl=tableModel.getList();
                            for (int i = 0; i < lsttbl.size(); i++) {
                                TableModel.list tableitem=lsttbl.get(i);
                                tableitem.setName(tableitem.getName());
                                tableitem.setId(tableitem.getId());
                                tableitem.setActive(tableitem.getActive());
                                tableItem.add(tableitem);
                                tableList.add(tableitem.getName());
                            }
//                            tableAdapter= new TableAdapter(getApplicationContext(),tableList);
                            spin.setItems(tableList);
//                            spin.setAdapter(tableAdapter);
                         

                        }else if (tableModel.getStatus_code().equals(Constants.Failure))
                        {

                            Toast.makeText(getApplicationContext(),tableModel.getStatus_code(),Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<TableModel> call, Throwable t) {

                }


            });

        }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isNetworkConnected != isConnected) {
            if (isConnected) {
            } else {
                AdditionalClass.showSnackBar(TableActivity.this);
            }
        }
        isNetworkConnected = isConnected;
    }
}
