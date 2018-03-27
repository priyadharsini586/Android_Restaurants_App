package com.nickteck.restaurantapp.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
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

        holder.list =catList.get(position);
        holder.mName.setText(holder.list.getName());
        Picasso.with(context)
                .load(holder.list.getImage())
                .placeholder(R.mipmap.ic_default_image)
                .into(holder.img, new Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.with(context)
                                .load(holder.list.getImage()) // image url goes here
                                .placeholder(R.mipmap.ic_default_image)
                                .into(holder.img);
                    }

                    @Override
                    public void onError() {
                    }
                });

    }

    @Override
    public int getItemCount() {
        return catList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mName;
        ItemListRequestAndResponseModel.list list;
        ImageView img;


        ViewHolder(View view) {
            super(view);
            mName=(TextView)view.findViewById(R.id.name);
            img=(ImageView)view.findViewById(R.id.image);

        }

    }
}
