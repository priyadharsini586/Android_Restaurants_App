package com.nickteck.restaurantapp.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 3/27/2018.
 */

public class CatagoryAdapter extends RecyclerView.Adapter<CatagoryAdapter.ViewHolder>{
    Context context;
    ArrayList<ItemListRequestAndResponseModel.list> catList;

    public CatagoryAdapter(Context context, ArrayList<ItemListRequestAndResponseModel.list> catList) {
        this.context = context;
        this.catList = catList;
    }

    @Override
    public CatagoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.catagory_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final CatagoryAdapter.ViewHolder holder, int position) {

        final ItemListRequestAndResponseModel.list list =catList.get(position);
        Log.e("item size", String.valueOf(list.getImage()));
        holder.mName.setText(list.getName());
        Picasso.with(context)
                .load(list.getImage())
                .placeholder(R.mipmap.ic_default_image)
                .into(holder.img);
        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return catList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mName;
        ImageView img;


        ViewHolder(View view) {
            super(view);
            mName=(TextView)view.findViewById(R.id.cat_name);
            img=(ImageView)view.findViewById(R.id.cat_image);

        }

    }
}
