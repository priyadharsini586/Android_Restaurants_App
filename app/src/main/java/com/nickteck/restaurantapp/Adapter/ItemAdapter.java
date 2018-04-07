package com.nickteck.restaurantapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.model.Constants;
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

    public ItemAdapter(ArrayList<ItemListRequestAndResponseModel.item_list> gridImageList, Context context) {
        this.gridImageList = gridImageList;
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
        runEnterAnimation(holder.itemView, position);



        holder.list =gridImageList.get(position);
        holder.mName.setText(holder.list.getItem_name());
        holder.mDescription.setText(holder.list.getDescription());
        holder.mPrice.setText("$"+holder.list.getPrice());
        Picasso.with(context)
                .load(holder.list.getImage()) // thumbnail url goes here
                .placeholder(R.drawable.cook8)
                .into(holder.img, new Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.with(context)
                                .load(holder.list.getImage()) // image url goes here
                                .placeholder(R.drawable.cook8)
                                .into(holder.img);
                    }

                    @Override
                    public void onError() {
                    }
             });


        if (holder.list.getFavourite().equals(Constants.Success)) {
            holder.favoriteImg.setImageResource(R.drawable.ic_favourite);
            holder.favoriteImg.setTag("yes");
        } else {
            holder.favoriteImg.setImageResource(R.drawable.ic_favourite_show);
            holder.favoriteImg.setTag("no");
        }

    }
    private void runEnterAnimation(View view, int position) {
        Animation animation = new TranslateAnimation(100, 0, 0, 0); // new TranslateAnimation (float fromXDelta,float toXDelta, float fromYDelta, float toYDelta)
        animation.setDuration(500);
        view .startAnimation(animation);


    }

    @Override
    public int getItemCount() {
        return gridImageList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mName,mDescription,mPrice;

        ItemListRequestAndResponseModel.item_list list;
        ImageView img,favoriteImg;


        ViewHolder(View view) {
            super(view);
            favoriteImg=(ImageView)view.findViewById(R.id.favorite);
            mName=(TextView)view.findViewById(R.id.name);
            mDescription=(TextView)view.findViewById(R.id.description);
            mPrice=(TextView)view.findViewById(R.id.price);
            img=(ImageView)view.findViewById(R.id.image);

    }

    }
}
