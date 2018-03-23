package com.nickteck.restaurantapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nickteck.restaurantapp.R;

/**
 * Created by admin on 3/23/2018.
 */

public class GridAdapter extends BaseAdapter {
    Context context;
    int cook[];
    LayoutInflater inflter;

    public GridAdapter(Context context, int[] cook) {
        this.context = context;
        this.cook = cook;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return cook.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.grid_layout, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        icon.setImageResource(cook[i]); // set logo images
        return view;

    }
}
