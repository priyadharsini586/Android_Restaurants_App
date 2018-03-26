package com.nickteck.restaurantapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 3/26/2018.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    ArrayList<ItemListRequestAndResponseModel.item_list> gridImageList;
    Context context;

    public ItemAdapter(ArrayList<ItemListRequestAndResponseModel.item_list> item_list, Context context) {
        this.gridImageList = item_list;
        this.context = context;
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemAdapter.ViewHolder holder, int position) {

        holder.list =gridImageList.get(position);
        holder.mName.setText(holder.list.getItem_name());
        holder.mDescription.setText(holder.list.getDescription());
        holder.mPrice.setText("$"+holder.list.getPrice());
        Picasso.with(context)
                .load(holder.list.getImage()) // thumbnail url goes here
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
        return gridImageList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mName,mDescription,mPrice;
        ItemListRequestAndResponseModel.item_list list;
        ImageView img;


        ViewHolder(View view) {
            super(view);
            mName=(TextView)view.findViewById(R.id.name);
            mDescription=(TextView)view.findViewById(R.id.description);
            mPrice=(TextView)view.findViewById(R.id.price);
            img=(ImageView)view.findViewById(R.id.image);

    }

    }
}
