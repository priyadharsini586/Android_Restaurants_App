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

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by admin on 4/6/2018.
 */

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {
    int itemCount;
    ArrayList<ItemListRequestAndResponseModel.item_list>item_lists;
    Context context;
    public MyOrdersAdapter(ArrayList<ItemListRequestAndResponseModel.item_list> item_lists, Context  context)
    {
        this.item_lists = item_lists;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_order_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.list = item_lists.get(position);
        Picasso.with(context)
                .load(holder.list.getImage())
                .placeholder(R.mipmap.ic_default_image)
                .into(holder.imgItem);
        holder.setIsRecyclable(false);
        holder.txtItemName.setText(holder.list.getItem_name());
        holder.txtItemDes.setText(holder.list.getDescription());
        holder.txtItemPrice.setText("Price: Rs."+holder.list.getPrice());
        holder.txtItemQty.setText("Qty: "+holder.list.getQty());
        holder.txtQty.setText(String.valueOf(holder.list.getQty()));
        double price = Double.valueOf(holder.list.getPrice());
        price = price*holder.list.getQty();
        holder.txtTotalPrice.setText("Total :Rs. "+String.valueOf(price));
        itemCount = holder.list.getQty();
        holder.imgIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCount = itemCount + 1;
                if (itemCount >= 1)
                {
                    holder.txtQty.setText(String.valueOf(itemCount));
                    holder.txtItemQty.setText("Qty: "+String.valueOf(itemCount));

                    double price = Double.valueOf(holder.list.getPrice());
                    price = price*itemCount;
                    holder.txtTotalPrice.setText("Total :Rs. "+String.valueOf(price));
//
                }
            }
        });

        holder.imgDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCount = itemCount - 1;
                if (itemCount >= 1)
                {
                    holder.txtQty.setText(String.valueOf(itemCount));
                    holder.txtItemQty.setText("Qty: "+String.valueOf(itemCount));

                    double price = Double.valueOf(holder.list.getPrice());
                    price = price*itemCount;
                    holder.txtTotalPrice.setText("Total :Rs. "+String.valueOf(price));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return item_lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgItem,imgIncrease,imgDecrease;
        public TextView txtItemName,txtItemDes,txtItemPrice,txtItemQty,txtQty,txtTotalPrice;
        ItemListRequestAndResponseModel.item_list list;
        ViewHolder(View view)
        {
            super(view);

            imgItem = (ImageView) view.findViewById(R.id.imgItem);
            txtItemName = (TextView) view.findViewById(R.id.txtItemName);
            txtItemDes = (TextView) view.findViewById(R.id.txtItemDes);
            txtItemPrice = (TextView) view.findViewById(R.id.txtItemPrice);
            txtItemQty= (TextView)view.findViewById(R.id.txtItemQty);
            txtQty = (TextView) view.findViewById(R.id.txtQty);
            txtTotalPrice = (TextView)view.findViewById(R.id.txtTotalPrice);
            imgDecrease = (ImageView) view.findViewById(R.id.imgDecrease);
            imgIncrease = (ImageView) view.findViewById(R.id.imgIncrease);
        }

    }
}
