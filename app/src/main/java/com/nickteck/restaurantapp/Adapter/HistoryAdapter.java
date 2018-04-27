package com.nickteck.restaurantapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 4/25/2018.
 */

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<ItemListRequestAndResponseModel.item_list>item_lists;
    Context mContext;
    private static final String TAG = HistoryAdapter.class.getSimpleName();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    public HistoryAdapter(ArrayList<ItemListRequestAndResponseModel.item_list>item_lists, Context context)
    {
        this.item_lists = item_lists;
        this.mContext = context;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item_layout, parent, false);
            return new HeaderViewHolder(layoutView);
        } else if (viewType == TYPE_ITEM) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_layout, parent, false);
            return new ItemViewHolder(layoutView);
        }

        throw new RuntimeException("No match for " + viewType + ".");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemListRequestAndResponseModel.item_list item_list = item_lists.get(position);
        if(holder instanceof HeaderViewHolder){
            ((HeaderViewHolder) holder).headerTitle.setText(item_list.getDate());
        }else if(holder instanceof ItemViewHolder){
            ((ItemViewHolder) holder).txtItemname.setText(item_list.getItem_name());
            ((ItemViewHolder) holder).txtItemDescription.setText(item_list.getDescription());
            ((ItemViewHolder) holder).txtItemPrice.setText(item_list.getPrice());
            Picasso.with(mContext)
                    .load(item_list.getImage())
                    .placeholder(R.mipmap.ic_default_image)
                    .into( ((ItemViewHolder) holder).txtItemImage);
        }
    }


    @Override
    public int getItemCount() {
        return item_lists.size();
    }
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }
    private boolean isPositionHeader(int position) {

        ItemListRequestAndResponseModel.item_list item_list = item_lists.get(position);
        if (item_list.getDate() == null)
        {
            return false;
        }else
        {
            return true;
        }

    }

    private ItemListRequestAndResponseModel.item_list getItem(int position) {
        return item_lists.get(position);
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txtItemname,txtItemPrice,txtItemDescription;
        ImageView txtItemImage;


        ItemViewHolder(View view) {
            super(view);

            txtItemname=(TextView)view.findViewById(R.id.txtItemname);
            txtItemDescription = (TextView)view.findViewById(R.id.txtItemDescription);
            txtItemPrice = (TextView) view.findViewById(R.id.txtItemPrice);
            txtItemImage=  (ImageView) view.findViewById(R.id.txtItemImage);

        }

    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView headerTitle;


        HeaderViewHolder(View view) {
            super(view);
            headerTitle = (TextView)view.findViewById(R.id.headerTitle);

        }

    }
}
