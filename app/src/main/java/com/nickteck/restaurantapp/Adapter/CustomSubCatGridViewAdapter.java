package com.nickteck.restaurantapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 5/8/2018.
 */

public class CustomSubCatGridViewAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<ItemListRequestAndResponseModel.cat_list> catLists;

    public CustomSubCatGridViewAdapter(Context context, ArrayList<ItemListRequestAndResponseModel.cat_list> catList) {
        mContext = context;
        this.catLists = catList;

    }

    @Override
    public int getCount() {
        return catLists.size();
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
    public View getView(int i, View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            ItemListRequestAndResponseModel.cat_list  cat_list = catLists.get(i);
            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.sub_cat_gridview_layout, null);
            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.android_gridview_image);
            textViewAndroid.setText(cat_list.getSub_Cat_name());
            Picasso.with(mContext)
                    .load(cat_list.getImage())
                    .placeholder(R.mipmap.ic_default_image)
                    .into(imageViewAndroid);
//            imageViewAndroid.setImageResource(gridViewImageId[i]);
        } else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }
}