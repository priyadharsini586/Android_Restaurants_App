package com.nickteck.restaurantapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.nickteck.restaurantapp.Adapter.TableAdapter;
import com.nickteck.restaurantapp.Db.Database;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.api.ApiClient;
import com.nickteck.restaurantapp.api.ApiInterface;
import com.nickteck.restaurantapp.model.AddWhislist;
import com.nickteck.restaurantapp.model.Constants;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.nickteck.restaurantapp.model.TableModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TableAdapter tableAdapter;
    ApiInterface apiInterface;
    Spinner spin;
    Database database;
    Button button;
    TableModel.list table;
    boolean data;
    private ArrayList<TableModel.list> tableList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        database=new Database(getApplicationContext());
        spin = (Spinner) findViewById(R.id.simpleSpinner);
        //button=(Button)findViewById(R.id.button);
        spin.setOnItemSelectedListener(this);
        getTabledata();

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.insertTable(table.getId(),table.getName());
                String text=database.getData();
                data=database.checkTables();
                if(data) {
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"data not available", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {

                table=tableList.get(i);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

                        if (tableModel.getStatus_code().equals(Constants.Success))
                        {
                            tableList = new ArrayList<TableModel.list>();
                            List<TableModel.list> lsttbl=tableModel.getList();
                            for (int i = 0; i < lsttbl.size(); i++) {
                                TableModel.list tableitem=lsttbl.get(i);
                                tableitem.setName(tableitem.getName());
                                tableitem.setId(tableitem.getId());
                                tableitem.setActive(tableitem.getActive());
                                tableList.add(tableitem);
                            }
                            tableAdapter= new TableAdapter(getApplicationContext(),tableList);
                            spin.setAdapter(tableAdapter);
                         

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


}
