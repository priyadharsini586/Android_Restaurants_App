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

import java.util.ArrayList;

/**
 * Created by admin on 4/7/2018.
 */

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder>{
    Context context;
    ArrayList name;
    ArrayList image;
    ArrayList description;
    ArrayList price;

    public FavouriteAdapter(Context context, ArrayList name, ArrayList image, ArrayList description, ArrayList price) {
        this.context = context;
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
    }

    @Override
    public FavouriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_layout, parent, false);
        return new FavouriteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavouriteAdapter.ViewHolder holder, int position) {
        holder.mName. setText((CharSequence) name.get(position));
        holder.mDescription.setText((CharSequence) description.get(position));
        holder.mPrice.setText((CharSequence) price.get(position));
        holder.img.setImageResource((Integer) image.get(position));
        holder.favoriteImg.setImageResource(R.drawable.ic_favourite);

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mName,mDescription,mPrice;
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
