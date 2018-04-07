package com.nickteck.restaurantapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nickteck.restaurantapp.Adapter.FavouriteAdapter;
import com.nickteck.restaurantapp.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by admin on 4/7/2018.
 */

public class FavouriteFragment extends Fragment {
    View mainView;
    RecyclerView recyclerView;
    FavouriteAdapter favouriteAdapter;
    TextView txtBrodgeIcon;
    // ArrayList for person names
    ArrayList name = new ArrayList<>(Arrays.asList("briyani", "Chicken", "Noodleas", "Prawn"));
    ArrayList image = new ArrayList<>(Arrays.asList(R.drawable.cook3, R.drawable.cook4, R.drawable.cook5, R.drawable.cook6));
    ArrayList description= new ArrayList<>(Arrays.asList("tasty","nice","good","yummy"));
    ArrayList price = new ArrayList<>(Arrays.asList("250.00","230.00","500.00","450.00"));

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.favourite_frag, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView tootBarTextViewb = (TextView)toolbar.findViewById(R.id.txtHomeToolBar);
        tootBarTextViewb.setText("Favourite");
        txtBrodgeIcon = (TextView)toolbar.findViewById(R.id.txtBrodgeIcon);
        txtBrodgeIcon.setVisibility(View.GONE);
        recyclerView=(RecyclerView)mainView.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        favouriteAdapter=new FavouriteAdapter(getActivity(),name,image,description,price);
        recyclerView.setAdapter(favouriteAdapter);
        return mainView;
    }
}
