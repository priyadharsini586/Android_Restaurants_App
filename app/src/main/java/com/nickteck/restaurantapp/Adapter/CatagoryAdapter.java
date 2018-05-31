package com.nickteck.restaurantapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    ArrayList<ItemListRequestAndResponseModel.cat_list> catList;
    private int lastPosition = -1;
    int row_index = -1;
    public CatagoryAdapter(Context context, ArrayList<ItemListRequestAndResponseModel.cat_list> catList) {
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
    public void onBindViewHolder(final CatagoryAdapter.ViewHolder holder, final int position) {


        final ItemListRequestAndResponseModel.cat_list list =catList.get(position);

        if (list.getSub_Cat_name() != null) {
            holder.mName.setText(list.getSub_Cat_name());
        }else if (list.getCat_name() != null)
        {
            holder.mName.setText(list.getCat_name());
        }
        Picasso.with(context)
                .load(list.getImage())
                .placeholder(R.mipmap.ic_default_image)
                .into(holder.img);
        holder.setIsRecyclable(false);


        holder.rldMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();
            }
        });
        if(row_index ==position){
            holder.rldMainMenu.setBackgroundColor(Color.parseColor("#567845"));
        }
        else
        {
            holder.rldMainMenu.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }


    @Override
    public int getItemCount() {
        return catList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mName;
        ImageView img;
        LinearLayout rldMainMenu;

        ViewHolder(View view) {
            super(view);
            mName=(TextView)view.findViewById(R.id.android_gridview_text);
            img=(ImageView)view.findViewById(R.id.android_gridview_image);
            rldMainMenu = (LinearLayout) view.findViewById(R.id.android_custom_gridview_layout);

        }

    }

    public void clearView()
    {
         row_index = -1;
    }
}
