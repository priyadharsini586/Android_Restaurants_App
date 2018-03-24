package com.nickteck.restaurantapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.activity.LoginActivity;
import com.nickteck.restaurantapp.model.Constants;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 3/23/2018.
 */

public class GridAdapter extends BaseAdapter {
    Context context;
   ArrayList<ItemListRequestAndResponseModel.list> grigImageList;
    LayoutInflater inflter;
    public GridAdapter(Context context, ArrayList<ItemListRequestAndResponseModel.list> grid) {
        this.context = context;
        this.grigImageList = grid;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return grigImageList.size();
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
        final ItemListRequestAndResponseModel.list item_list = grigImageList.get(i);

        if (view == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.grid_layout, null);

        }
        final ImageView gridImageView = (ImageView) view.findViewById(R.id.gridImageView);
        TextView txtCatName = (TextView) view.findViewById(R.id.gridCatTextView);
        txtCatName.setText(item_list.getName());
        final String listImage = Constants.CATEGORY_BASE_URL + item_list.getImage();
        Log.e("image loader url",listImage);

        Picasso.with(context)
                .load(listImage) // thumbnail url goes here
                .placeholder(R.mipmap.ic_default_image)
                .into(gridImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.with(context)
                                .load(listImage) // image url goes here
                                .placeholder(R.mipmap.ic_default_image)
                                .into(gridImageView);
                    }

                    @Override
                    public void onError() {
                    }
                });
//        Picasso.with(context).load(listImage).into(gridImageView);

        return view;

    }


}
