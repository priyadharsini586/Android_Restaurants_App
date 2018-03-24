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
import com.nickteck.restaurantapp.image_cache.ImageLoader;
import com.nickteck.restaurantapp.model.Constants;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;

import java.util.ArrayList;

/**
 * Created by admin on 3/23/2018.
 */

public class GridAdapter extends BaseAdapter {
    Context context;
   ArrayList<ItemListRequestAndResponseModel.list> grigImageList;
    LayoutInflater inflter;
    ImageLoader imageLoader;
    public GridAdapter(Context context, ArrayList<ItemListRequestAndResponseModel.list> grid) {
        this.context = context;
        this.grigImageList = grid;
        inflter = (LayoutInflater.from(context));
        imageLoader=new ImageLoader(context.getApplicationContext());
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
        ImageView gridImageView = (ImageView) view.findViewById(R.id.gridImageView);
        TextView txtCatName = (TextView) view.findViewById(R.id.gridCatTextView);
        txtCatName.setText(item_list.getName());
        String listImage = Constants.CATEGORY_BASE_URL + item_list.getImage();
        imageLoader.DisplayImage(listImage,gridImageView,R.mipmap.ic_default_image);

        return view;

    }


}
