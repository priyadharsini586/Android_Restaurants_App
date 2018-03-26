package com.nickteck.restaurantapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.nickteck.restaurantapp.Adapter.GridAdapter;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.additional_class.AdditionalClass;
import com.nickteck.restaurantapp.api.ApiClient;
import com.nickteck.restaurantapp.api.ApiInterface;
import com.nickteck.restaurantapp.model.Constants;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.nickteck.restaurantapp.network.ConnectivityReceiver;
import com.nickteck.restaurantapp.network.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    GridView gridView;
    boolean isNetworkConnected;
    ApiInterface apiInterface;
    ArrayList<ItemListRequestAndResponseModel.list> gridImageList;
    String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        gridView=(GridView)findViewById(R.id.simpleGridView);

        itemId= getIntent().getStringExtra("itemId");
        Log.e("item id",itemId);

        checkConnection();
        MyApplication.getInstance().setConnectivityListener(this);


    }
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
            isNetworkConnected = false;
        }else
        {
            isNetworkConnected = true;
            getItemView();

        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {


//        if (isNetworkConnected != isConnected) {
            if (isConnected) {
                Toast.makeText(getApplicationContext(), "Network Connected", Toast.LENGTH_LONG).show();
                getItemView();
            } else {
                Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
                AdditionalClass.showSnackBar(ItemActivity.this);

            }
//        }
        isNetworkConnected = isConnected;

    }

    public void getItemView()
    {
        if (isNetworkConnected)
        {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            JSONObject getJsonObject = new JSONObject();
            try {
                getJsonObject.put("cat_id",itemId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Call<ItemListRequestAndResponseModel> getCatageoryList = apiInterface.getItemList(getJsonObject);
            getCatageoryList.enqueue(new Callback<ItemListRequestAndResponseModel>() {
                @Override
                public void onResponse(Call<ItemListRequestAndResponseModel> call, Response<ItemListRequestAndResponseModel> response) {
                    if (response.isSuccessful())
                    {
                        ItemListRequestAndResponseModel itemListRequestAndResponseModel = response.body();
                        if (itemListRequestAndResponseModel.getStatus_code().equals(Constants.Success))
                        {
                            gridImageList = new ArrayList<>();
                            ArrayList getItemDetils = itemListRequestAndResponseModel.getItem_list();
                          /*  for (int i = 0; i < getItemDetils.size(); i++) {
                                ItemListRequestAndResponseModel.item_list  categoryList = (ItemListRequestAndResponseModel.list) getItemDetils.get(i);
                                gridImageList.add(categoryList);
                            }
                            GridAdapter gridAdapter=new GridAdapter(getApplicationContext(),gridImageList);
                            gridView.setAdapter(gridAdapter);*/
                        }else if (itemListRequestAndResponseModel.getStatus_code().equals(Constants.Failure))
                        {
                            Toast.makeText(getApplicationContext(),itemListRequestAndResponseModel.getStatus_message(),Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ItemListRequestAndResponseModel> call, Throwable t) {

                }
            });
        }
    }
}
