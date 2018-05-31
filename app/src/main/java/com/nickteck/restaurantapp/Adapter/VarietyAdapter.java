package com.nickteck.restaurantapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;

import java.util.ArrayList;

/**
 * Created by admin on 4/21/2018.
 */

public class VarietyAdapter extends RecyclerView.Adapter<VarietyAdapter.VarietyViewHolder> {

    Context context;
    ArrayList<ItemListRequestAndResponseModel.Variety_id_list> varietyIdLists;
    int row_index = -1;
    public VarietyAdapter(Context context, ArrayList<ItemListRequestAndResponseModel.Variety_id_list> varietyIdLists) {
        this.context = context;
        this.varietyIdLists = varietyIdLists;
    }


    @Override
    public VarietyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.variety_list, parent, false);
        return new VarietyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VarietyViewHolder holder, final int position) {
        ItemListRequestAndResponseModel.Variety_id_list list =varietyIdLists.get(position);
        holder.txtVarityName.setText(list.getVariety_name());

        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 row_index = position;
                notifyDataSetChanged();
            }
        });
        if(row_index ==position){
            holder.mainView.setBackgroundColor(Color.parseColor("#ff5500"));

        }
        else
        {
            holder.mainView.setBackgroundColor(Color.parseColor("#ffccb2"));

        }

    }

    @Override
    public int getItemCount() {
        return varietyIdLists.size();
    }

    public int getRow_index() {
        return row_index;
    }

    public class VarietyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtVarityName;
        ConstraintLayout mainView;

        VarietyViewHolder(View view) {
            super(view);
            txtVarityName=(TextView)view.findViewById(R.id.txtVarityName);
            mainView=(ConstraintLayout)view.findViewById(R.id.mainView);

        }

    }

    public  void clearView()
    {
        row_index = -1;
    }
}
