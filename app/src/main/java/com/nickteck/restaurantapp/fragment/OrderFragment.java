package com.nickteck.restaurantapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nickteck.restaurantapp.Adapter.OrderAdapter;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.nickteck.restaurantapp.model.ItemModel;

import java.util.ArrayList;


public class OrderFragment extends Fragment {

    View mainView;
    ItemModel  itemModel = ItemModel.getInstance();
    ArrayList<ItemListRequestAndResponseModel.item_list> itemLists;
    OrderAdapter ordersAdapter;
    RecyclerView orderRecycleView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.order_fragment, container, false);
        itemLists = itemModel.getListArrayList();
        orderRecycleView = (RecyclerView) mainView.findViewById(R.id.orderRecycleView);

        ordersAdapter=new OrderAdapter(getActivity(),itemLists);
        orderRecycleView.setAdapter(ordersAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        orderRecycleView.setLayoutManager(linearLayoutManager);
        ordersAdapter.notifyDataSetChanged();

        return mainView;
    }


}
