package com.nickteck.restaurantapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nickteck.restaurantapp.Adapter.FavouriteAdapter;
import com.nickteck.restaurantapp.Db.Database;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.additional_class.AdditionalClass;
import com.nickteck.restaurantapp.api.ApiClient;
import com.nickteck.restaurantapp.api.ApiInterface;
import com.nickteck.restaurantapp.model.Constants;
import com.nickteck.restaurantapp.model.FavouriteCustomList;
import com.nickteck.restaurantapp.model.FavouriteListData;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.nickteck.restaurantapp.model.LoginRequestAndResponse;
import com.nickteck.restaurantapp.network.ConnectivityReceiver;
import com.nickteck.restaurantapp.network.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nickteck.restaurantapp.model.Constants.ITEM_BASE_URL;

/**
 * Created by admin on 4/7/2018.
 */

public class FavouriteFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {
    View mainView;
    RecyclerView recyclerView;
    FavouriteAdapter favouriteAdapter;
    TextView txtBrodgeIcon;
    boolean isNetworkConnected;
    ApiInterface apiInterface;
    Database database;
    ArrayList<FavouriteListData.FavouriteListDetails> favouriteListDetails_adapter;
    private  ArrayList<ItemListRequestAndResponseModel.item_list> gridImageList;
    ArrayList<FavouriteCustomList> final_arrayList;
    FavouriteCustomList favouriteCustomList;
    private ProgressBar progress_ratings;
    private TextView txtfavouriteList;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        MyApplication.getInstance().setConnectivityListener(this);
        if (AdditionalClass.isNetworkAvailable(getActivity())) {
            isNetworkConnected = true;
        }else {
            isNetworkConnected = false;
        }

        mainView = inflater.inflate(R.layout.favourite_frag, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView tootBarTextViewb = (TextView)toolbar.findViewById(R.id.txtHomeToolBar);
        progress_ratings  = (ProgressBar) mainView.findViewById(R.id.progress_ratings);
        txtfavouriteList = (TextView) mainView.findViewById(R.id.txtfavouriteList);
        txtfavouriteList.setVisibility(View.GONE);
        tootBarTextViewb.setText("Favourite");
        database = new Database(getActivity());
        favouriteListDetails_adapter = new ArrayList<>();
        gridImageList = new ArrayList<>();
        final_arrayList = new ArrayList<>();

        txtBrodgeIcon = (TextView)toolbar.findViewById(R.id.txtBrodgeIcon);
        txtBrodgeIcon.setVisibility(View.GONE);
        recyclerView=(RecyclerView)mainView.findViewById(R.id.recycler_view);

        if(isNetworkConnected){
            progress_ratings.setVisibility(View.VISIBLE);
            getItemList();
        }else {
            AdditionalClass.showSnackBar(getActivity());
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
       // favouriteAdapter=new FavouriteAdapter(getActivity(),favouriteListDetails_adapter);
        favouriteAdapter=new FavouriteAdapter(getActivity(),final_arrayList,FavouriteFragment.this);

        recyclerView.setAdapter(favouriteAdapter);

        return mainView;
    }


    private void getItemList() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ItemListRequestAndResponseModel> getCatageoryList = apiInterface.getItemData();
        getCatageoryList.enqueue(new Callback<ItemListRequestAndResponseModel>() {

            @Override
            public void onResponse(Call<ItemListRequestAndResponseModel> call, Response<ItemListRequestAndResponseModel> response) {
                ItemListRequestAndResponseModel itemListRequestAndResponseModel = response.body();
                if (itemListRequestAndResponseModel.getStatus_code().equals(Constants.Success)) {
                    List<ItemListRequestAndResponseModel.item_list> getItemDetils = itemListRequestAndResponseModel.getItem_list();
                    for (int i = 0; i < getItemDetils.size(); i++) {
                        ItemListRequestAndResponseModel.item_list items = getItemDetils.get(i);
                        items.setItem_name(items.getItem_name());
                        items.setDescription(items.getDescription());
                        items.setPrice(items.getPrice());
                        items.setImage(items.getImage());
                        String url = ITEM_BASE_URL + items.getImage();
                        Log.e("sub catagory url", url);
                        items.setFavourite("0");
                        items.setImage(url);
                        gridImageList.add(items);
                    }
                    send_Customer_id_api();
                }
            }

            @Override
            public void onFailure(Call<ItemListRequestAndResponseModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void send_Customer_id_api() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", database.getCustomerId());

        }catch (JSONException e){
            e.printStackTrace();
        }
        final Call<FavouriteListData> favouriteListDataCall = apiInterface.FavouriteListDetails(jsonObject);
        favouriteListDataCall.enqueue(new Callback<FavouriteListData>() {
            @Override
            public void onResponse(Call<FavouriteListData> call, Response<FavouriteListData> response) {
                if (response.isSuccessful()){
                    if(response.body().getStatus_code().equals("1")){
                        FavouriteListData favouriteListData = response.body();
                        for(int i=0;i<favouriteListData.getList().size();i++){
                            favouriteListData.getList().get(i).getSno();
                            favouriteListData.getList().get(i).getItem_id();
                            FavouriteListData.FavouriteListDetails favouriteListDetails =  favouriteListData.getList().get(i);
                            favouriteListDetails_adapter.add(favouriteListDetails);
                        }
                        checkForMatchingData();

                    }else {
                        progress_ratings.setVisibility(View.GONE);
                       txtfavouriteList.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onFailure(Call<FavouriteListData> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void checkForMatchingData() {

        for(int i=0; i<favouriteListDetails_adapter.size() ;i++){
            for(ItemListRequestAndResponseModel.item_list s : gridImageList){
               if(favouriteListDetails_adapter.get(i).getItem_id().equals(s.getItem_id())){

                   // getting sno only from the favouriteList Array
                   String  getSno = favouriteListDetails_adapter.get(i).getSno();

                   String getItemId = s.getItem_id();
                   String ItemName = s.getItem_name();
                   String getDescription = s.getDescription();
                   String getItemImage = s.getImage();
                   String getItemPrice = s.getPrice();

                   favouriteCustomList = new FavouriteCustomList(getSno,getItemId,ItemName,getDescription,getItemImage,getItemPrice);
                   final_arrayList.add(favouriteCustomList);
               }else {
                  // Toast.makeText(getActivity(), "Item id not matched", Toast.LENGTH_SHORT).show();
               }
            }
        }
        favouriteAdapter.notifyDataSetChanged();
        progress_ratings.setVisibility(View.GONE);
    }

    public void removeFavouriteItemApi(String delete_favourite_item_id, final int pos){
        progress_ratings.setVisibility(View.VISIBLE);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", database.getCustomerId());
            jsonObject.put("item_id", delete_favourite_item_id);

        }catch (JSONException e){
            e.printStackTrace();
        }
        final Call<LoginRequestAndResponse> listDataCall = apiInterface.FavouriteDelete(jsonObject);
        listDataCall.enqueue(new Callback<LoginRequestAndResponse>() {
            @Override
            public void onResponse(Call<LoginRequestAndResponse> call, Response<LoginRequestAndResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatusCode().equals("1")) {
                        Toast.makeText(getActivity(), "Favorite Deleted Successfully", Toast.LENGTH_SHORT).show();

                      //  favouriteAdapter.notifyDataSetChanged();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final_arrayList.remove(pos);
                                favouriteAdapter.notifyDataSetChanged();
                                Log.e("list size", String.valueOf(final_arrayList.size()));
                                progress_ratings.setVisibility(View.GONE);
                                if(final_arrayList.size()==0){
                                    txtfavouriteList.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                        progress_ratings.setVisibility(View.GONE);
                    }else {
                        Toast.makeText(getActivity(), "Favorite Not Exists", Toast.LENGTH_SHORT).show();
                        progress_ratings.setVisibility(View.GONE);
                    }

                }else {
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<LoginRequestAndResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();

            }
        });




    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isNetworkConnected != isConnected) {
            if (isConnected) {
                Toast.makeText(getActivity(), "Network Connected", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getActivity(), "Network not available", Toast.LENGTH_LONG).show();
                AdditionalClass.showSnackBar(getActivity());

            }
        }
        isNetworkConnected = isConnected;
    }
}
