package com.nickteck.restaurantapp.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.nickteck.restaurantapp.Adapter.CatagoryAdapter;
import com.nickteck.restaurantapp.Adapter.GridAdapter;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.api.ApiClient;
import com.nickteck.restaurantapp.api.ApiInterface;
import com.nickteck.restaurantapp.model.Constants;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nickteck.restaurantapp.model.Constants.CATEGORY_BASE_URL;
import static com.nickteck.restaurantapp.model.Constants.ITEM_BASE_URL;

/**
 * Created by admin on 3/27/2018.
 */

public class CatagoryFragment extends Fragment {
    View view;
    ApiInterface apiInterface;
    RecyclerView catagory;
    ArrayList<ItemListRequestAndResponseModel.list> catList;
    @SuppressLint("ResourceType")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cat, container, false);
        catagory=(RecyclerView)view.findViewById(R.id.recycler_view);
        getCategoryData();
        return view;
    }
    public void getCategoryData()
    {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JSONObject getJsonObject = new JSONObject();
        try {
            getJsonObject.put("from",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<ItemListRequestAndResponseModel> getCatageoryList = apiInterface.getCatagoryList(getJsonObject);
        getCatageoryList.enqueue(new Callback<ItemListRequestAndResponseModel>() {
            @Override
            public void onResponse(Call<ItemListRequestAndResponseModel> call, Response<ItemListRequestAndResponseModel> response) {
                if (response.isSuccessful())
                {
                    ItemListRequestAndResponseModel itemListRequestAndResponseModel = response.body();
                    if (itemListRequestAndResponseModel.getStatus_code().equals(Constants.Success))
                    {
                        catList = new ArrayList<>();
                        ArrayList getItemDetils = itemListRequestAndResponseModel.getList();
                        for (int i = 0; i < getItemDetils.size(); i++) {
                            ItemListRequestAndResponseModel.list  categoryList = (ItemListRequestAndResponseModel.list) getItemDetils.get(i);

                            categoryList.setName(categoryList.getName());
                            String url=CATEGORY_BASE_URL+categoryList.getImage();
                            categoryList.setImage(url);
                            catList.add(categoryList);
                        }
                        CatagoryAdapter gridAdapter=new CatagoryAdapter(getActivity(),catList);
                        catagory.setAdapter(gridAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        catagory.setLayoutManager(linearLayoutManager);
                        gridAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(Call<ItemListRequestAndResponseModel> call, Throwable t) {

            }
        });
    }

}
