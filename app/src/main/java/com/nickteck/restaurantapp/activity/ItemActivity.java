package com.nickteck.restaurantapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.nickteck.restaurantapp.Adapter.GridAdapter;
import com.nickteck.restaurantapp.Adapter.ItemAdapter;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nickteck.restaurantapp.model.Constants.CATEGORY_BASE_URL;
import static com.nickteck.restaurantapp.model.Constants.ITEM_BASE_URL;

public class ItemActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    RecyclerView recyclerView;
    boolean isNetworkConnected;
    ApiInterface apiInterface;
    ItemAdapter itemAdapter;
    private  ArrayList<ItemListRequestAndResponseModel.item_list> gridImageList=new ArrayList<>();
    String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

//        itemId= getIntent().getStringExtra("itemId");
//        Log.e("item id",itemId);

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
                getJsonObject.put("cat_id",45);
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
                            gridImageList = new ArrayList<ItemListRequestAndResponseModel.item_list>();
                            List<ItemListRequestAndResponseModel.item_list> getItemDetils = itemListRequestAndResponseModel.getItem_list();
                            for (int i = 0; i < getItemDetils.size(); i++) {
                                ItemListRequestAndResponseModel.item_list items=getItemDetils.get(i);
                                items.setItem_name(items.getItem_name());
                                items.setDescription(items.getDescription());
                                items.setPrice(items.getPrice());
                                items.setImage(items.getImage());
                                String url=ITEM_BASE_URL+items.getImage();
                                Log.e("url",url);
                                items.setImage(url);
                                gridImageList.add(items);

                            }
                            itemAdapter=new ItemAdapter(gridImageList,getApplicationContext());
                            recyclerView.setAdapter(itemAdapter);
                            itemAdapter.notifyDataSetChanged();

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
