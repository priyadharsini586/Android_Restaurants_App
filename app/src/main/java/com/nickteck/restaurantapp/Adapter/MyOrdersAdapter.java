package com.nickteck.restaurantapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.nickteck.restaurantapp.model.ItemModel;
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
    private Callback listener;

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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
                if (itemCount >= 1)
                    itemCount = itemCount + 1;
                Log.e("count", String.valueOf(itemCount));
                if (itemCount >= 1)
                {
                    holder.txtQty.setText(String.valueOf(itemCount));
                    holder.txtItemQty.setText("Qty: "+String.valueOf(itemCount));

                    double price = Double.valueOf(holder.list.getPrice());
                    price = price*itemCount;
                    holder.txtTotalPrice.setText("Total :Rs. "+String.valueOf(price));

                    holder.list.setQty(itemCount);
                    ItemModel itemModel = ItemModel.getInstance();
                    itemModel.getListArrayList().set(position,holder.list);
                    listener.itemIncreased(price);
//
                }
            }
        });

        holder.imgDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemCount >= 2)
                    itemCount = itemCount - 1;
                Log.e("count", String.valueOf(itemCount));
                if (itemCount >= 1)
                {
                    holder.txtQty.setText(String.valueOf(itemCount));
                    holder.txtItemQty.setText("Qty: "+String.valueOf(itemCount));

                    double price = Double.valueOf(holder.list.getPrice());
                    price = price*itemCount;
                    holder.txtTotalPrice.setText("Total :Rs. "+String.valueOf(price));

                    holder.list.setQty(itemCount);
                    ItemModel itemModel = ItemModel.getInstance();
                    itemModel.getListArrayList().set(position,holder.list);
                    listener.itemDecreased(price);
                }
            }
        });

        holder.ldtRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(position);
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
        LinearLayout ldtRemoveItem;
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
            ldtRemoveItem = (LinearLayout)view.findViewById(R.id.ldtRemoveItem);
        }

    }

    public void openDialog(final int pos)
    {
        new AlertDialog.Builder(context)
                .setTitle("Conformation?")
                .setMessage("Do you want to remove this item?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeAt(pos);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("MainActivity", "Aborting mission...");
                    }
                })
                .show();
    }


    public void removeAt(int position) {
        item_lists.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, item_lists.size());
        notifyDataSetChanged();
        // here
        if(listener!=null)
        {
            listener.onChangeItemCount(item_lists.size());
        }
    }
    public interface Callback
    {
        public void onChangeItemCount(int totaltcount);
        public void itemIncreased(double count);
        public void itemDecreased(double count);
    }

    public void setListener(Callback listener)    {
        this.listener = listener;
    }
}
