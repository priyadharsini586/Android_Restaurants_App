package com.nickteck.restaurantapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nickteck.restaurantapp.Adapter.HistoryAdapter;
import com.nickteck.restaurantapp.Adapter.ItemAdapter;
import com.nickteck.restaurantapp.Db.Database;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.activity.LoginActivity;
import com.nickteck.restaurantapp.api.ApiClient;
import com.nickteck.restaurantapp.api.ApiInterface;
import com.nickteck.restaurantapp.model.Constants;
import com.nickteck.restaurantapp.model.HistoryModel;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nickteck.restaurantapp.model.Constants.ITEM_BASE_URL;


public class HistoryFragment extends Fragment {

    View mainView;
    RecyclerView myHistoryRecycleView;
    ApiInterface apiInterface;
    TextView txtNoHostory;
    ProgressBar progressHistory;
    ArrayList<ItemListRequestAndResponseModel.item_list> historyList;
    HashMap<String, ItemListRequestAndResponseModel.item_list> itemListDetails;
    HistoryAdapter historyAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_history, container, false);
        myHistoryRecycleView =(RecyclerView) mainView.findViewById(R.id.myHistoryRecycleView);
        myHistoryRecycleView.setVisibility(View.GONE);
        txtNoHostory  = (TextView) mainView.findViewById(R.id.txtNoHostory);
        txtNoHostory.setVisibility(View.GONE);
        progressHistory = (ProgressBar)mainView.findViewById(R.id.progressHistory);
        progressHistory.setVisibility(View.GONE);
        getItemDetails();

        return mainView;
    }

    private void getItemDetails() {

            apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Call<ItemListRequestAndResponseModel> getCatageoryList = apiInterface.getItemData();
            getCatageoryList.enqueue(new Callback<ItemListRequestAndResponseModel>() {
                @Override
                public void onResponse(Call<ItemListRequestAndResponseModel> call, Response<ItemListRequestAndResponseModel> response) {
                    if (response.isSuccessful())
                    {
                        ItemListRequestAndResponseModel itemListRequestAndResponseModel = response.body();
                        if (itemListRequestAndResponseModel.getStatus_code().equals(Constants.Success))
                        {

                             itemListDetails = new HashMap<>();

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
                                itemListDetails.put(items.getItem_id(),items);

                            }

                            getHistoryDetails();
                        }else if (itemListRequestAndResponseModel.getStatus_code().equals(Constants.Failure))
                        {
                            Toast.makeText(getActivity(),itemListRequestAndResponseModel.getStatus_message(),Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ItemListRequestAndResponseModel> call, Throwable t) {

                }
            });


    }

    private void getHistoryDetails() {
        progressHistory.setVisibility(View.VISIBLE);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JSONObject jsonObject = new JSONObject();
        Database database =new Database(getActivity());
        try {
            jsonObject.put("customer_id","48");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<HistoryModel>getHistoryList = apiInterface.getHistoryDetails(jsonObject);
        getHistoryList.enqueue(new Callback<HistoryModel>() {
            @Override
            public void onResponse(Call<HistoryModel> call, Response<HistoryModel> response) {
                if (response.isSuccessful())
                {
                    HistoryModel historyModel = response.body();
                    if (historyModel.getStatus_code().equals(Constants.Success))
                    {
                        txtNoHostory.setVisibility(View.GONE);
                        myHistoryRecycleView.setVisibility(View.VISIBLE);
                        historyList = new ArrayList<ItemListRequestAndResponseModel.item_list>();
                        ArrayList<HistoryModel.item_list> itemListDate = historyModel.getItem_list();
                        for (int j = 0 ; j < itemListDate.size();j++)
                        {
                            HistoryModel.item_list item_list = itemListDate.get(j);
                            String historyDate = item_list.getDate();
                            ItemListRequestAndResponseModel.item_list itemDate = new ItemListRequestAndResponseModel.item_list ();
                            itemDate.setDate(historyDate);
                            historyList.add(itemDate);
                            for (int k = 0; k < item_list.getItems().size() ; k ++)
                            {
                                HistoryModel.items items = item_list.getItems().get(k);
                                historyList.add(itemListDetails.get(items.getItem_id()));
                            }
                        }

                        historyAdapter=new HistoryAdapter(historyList,getActivity());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        myHistoryRecycleView.setLayoutManager(linearLayoutManager);
                        myHistoryRecycleView.setAdapter(historyAdapter);
                        historyAdapter.notifyDataSetChanged();
                    }else
                    {
                        txtNoHostory.setVisibility(View.VISIBLE);
                        myHistoryRecycleView.setVisibility(View.GONE);
                    }
                    progressHistory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<HistoryModel> call, Throwable t) {

            }
        });
    }


}
