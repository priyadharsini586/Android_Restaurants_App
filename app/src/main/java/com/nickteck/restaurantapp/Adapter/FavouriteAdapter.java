package com.nickteck.restaurantapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.fragment.FavouriteFragment;
import com.nickteck.restaurantapp.model.Constants;
import com.nickteck.restaurantapp.model.FavouriteCustomList;
import com.nickteck.restaurantapp.model.FavouriteListData;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 4/7/2018.
 */

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder>{

    Context context;
    ArrayList<FavouriteCustomList> final_arrayList;
    FavouriteFragment mfavouriteFragment;

    public FavouriteAdapter(Context context, ArrayList<FavouriteCustomList> final_arrayList, FavouriteFragment favouriteFragment) {
        this.context = context;
        this.final_arrayList = final_arrayList;
        this.mfavouriteFragment = favouriteFragment;
    }

    @Override
    public FavouriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_layout, parent, false);
        return new FavouriteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavouriteAdapter.ViewHolder holder,final int position) {

        holder.mName.setText(final_arrayList.get(position).getName());
        holder.mPrice.setText(final_arrayList.get(position).getItem_price());
        holder.mDescription.setText(final_arrayList.get(position).getDescription());

        Picasso.with(context)
                .load(final_arrayList.get(position).getImage_url()) // image url goes here
                .placeholder(R.mipmap.ic_default_image)
                .into(holder.img);

        holder.favoriteImg_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialoge(final_arrayList.get(position).getItem_id(),position);
            }
        });

    }

    private void alertDialoge(String item_id, final int position) {
        final String delete_favourite_item_id = item_id;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Are You Sure to Remove This Item from Your Favourite List ?");


        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mfavouriteFragment.removeFavouriteItemApi(delete_favourite_item_id,position);

                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


    @Override
    public int getItemCount() {
        return final_arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mName,mDescription,mPrice;
        ImageView img,favoriteImg_cancel_button;

        ViewHolder(View view) {
            super(view);
            favoriteImg_cancel_button=(ImageView)view.findViewById(R.id.favorite_cancel_button);
            mName=(TextView)view.findViewById(R.id.name);
            mDescription=(TextView)view.findViewById(R.id.description);
            mPrice=(TextView)view.findViewById(R.id.price);
            img=(ImageView)view.findViewById(R.id.image);

        }
    }
}
