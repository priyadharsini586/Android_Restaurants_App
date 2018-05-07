package com.nickteck.restaurantapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.nickteck.restaurantapp.model.ItemModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 5/7/2018.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

   private Context mContext;
   ArrayList<ItemListRequestAndResponseModel.item_list>itemModels;
    public OrderAdapter(Context mContext,ArrayList<ItemListRequestAndResponseModel.item_list>itemModels)
    {
        this.mContext = mContext;
        this.itemModels = itemModels;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        holder.list = itemModels.get(position);
        Picasso.with(mContext)
                .load(holder.list.getImage())
                .placeholder(R.mipmap.ic_default_image)
                .into(holder.imgOrder);
        holder.itemName.setText(holder.list.getItem_name());
        holder.itemDescription.setText(holder.list.getDescription());
        holder.itemPrice.setText("Price: Rs."+holder.list.getPrice());
        holder.txtTotalQty.setText(String.valueOf(holder.list.getQty()));
        holder.imgNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(itemModels.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ItemListRequestAndResponseModel.item_list list;
        ImageView imgOrder,imgNotes;
        TextView itemName,itemDescription,itemPrice,txtTotalQty;
        public ViewHolder(View itemView) {
            super(itemView);

            imgOrder= (ImageView) itemView.findViewById(R.id.imgOrder);
            itemName = (TextView) itemView.findViewById(R.id.itemName);
            itemDescription = (TextView) itemView.findViewById(R.id.itemDescription);
            itemPrice = (TextView) itemView.findViewById(R.id.itemPrice);
            txtTotalQty = (TextView) itemView.findViewById(R.id.txtTotalQty);
            imgNotes = (ImageView) itemView.findViewById(R.id.imgNotes);
        }
    }

    private void openDialog(final ItemListRequestAndResponseModel.item_list item_list) {
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Notes");
            final TextView input = new TextView(mContext);
            input.setTextColor(mContext.getResources().getColor(R.color.black_color));
            input.setGravity(Gravity.CENTER | Gravity.BOTTOM);
            input.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            alertDialog.setView(input);
            if (item_list.getNotes() != null) {
                if (!item_list.getNotes().isEmpty())
                    input.setText(item_list.getNotes());
            }

            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            if (item_list.getNotes() != null) {
                if (!item_list.getNotes().isEmpty())
                    alertDialog.show();
            }

        }
    }
}
