package com.nickteck.restaurantapp.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
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

public class TableActivity extends AppCompatActivity {
    TableAdapter tableAdapter;
    ApiInterface apiInterface;
    MaterialSpinner spin;
    Database database;
    Button button;
    TableModel.list table;
    private ArrayList<String> tableList = new ArrayList<>();
    private ArrayList<TableModel.list> tableItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        database = new Database(getApplicationContext());
        spin = (MaterialSpinner) findViewById(R.id.tableSpinner);
        //button=(Button)findViewById(R.id.button);

        getTabledata();

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (table != null) {
                    database.insertTable(table.getId(), table.getName());
                    Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(TableActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
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
                    Toast.makeText(getApplicationContext(), "Please select valid date", Toast.LENGTH_LONG).show();

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


}
