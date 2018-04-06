package com.nickteck.restaurantapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nickteck.restaurantapp.Adapter.MyOrdersAdapter;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.model.ItemModel;


public class MyOrdersFragment extends Fragment {


    View mainView;
    TextView txtBrodgeIcon;
    ItemModel itemModel;
    RecyclerView myOrderRecycleView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView =  inflater.inflate(R.layout.fragment_my_orders, container, false);
        // Inflate the layout for this fragment
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView tootBarTextViewb = (TextView)toolbar.findViewById(R.id.txtHomeToolBar);
        String content_text = getResources().getString(R.string.my_order);
        tootBarTextViewb.setText(content_text);
        txtBrodgeIcon = (TextView)toolbar.findViewById(R.id.txtBrodgeIcon);
        itemModel = ItemModel.getInstance();
        if (itemModel.getListArrayList().size() == 0)
        {
            txtBrodgeIcon.setVisibility(View.GONE);
        }else
        {
            txtBrodgeIcon.setVisibility(View.VISIBLE);
            txtBrodgeIcon.setText(String.valueOf(itemModel.getListArrayList().size()));
        }

        Log.e("itemList", String.valueOf(itemModel.getListArrayList().size()));

        myOrderRecycleView = (RecyclerView) mainView.findViewById(R.id.myOrderRecycleView);

        MyOrdersAdapter gridAdapter=new MyOrdersAdapter(itemModel.getListArrayList(),getActivity());
        myOrderRecycleView.setAdapter(gridAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        myOrderRecycleView.setLayoutManager(linearLayoutManager);
        gridAdapter.notifyDataSetChanged();

        return mainView;
    }


}
