package com.nickteck.restaurantapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nickteck.restaurantapp.Db.Database;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.additional_class.AdditionalClass;
import com.nickteck.restaurantapp.api.ApiInterface;
import com.nickteck.restaurantapp.fragment.OrderTakenScreenFragment;
import com.nickteck.restaurantapp.interfaceFol.ItemListener;
import com.nickteck.restaurantapp.model.Constants;
import com.nickteck.restaurantapp.model.FavouriteCustomList;
import com.nickteck.restaurantapp.model.FavouriteListData;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 3/26/2018.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    ArrayList<ItemListRequestAndResponseModel.item_list> gridImageList;

    Context context;
    Activity activity;
    OrderTakenScreenFragment morderTakenFragment;
    ArrayList<FavouriteListData.FavouriteListDetails> mfavouriteArrayList;
    public ImageView favouriate_iamge;

    public ItemAdapter(ArrayList<ItemListRequestAndResponseModel.item_list> gridImageList, ArrayList<FavouriteListData.FavouriteListDetails>
            favouriteArrayList, Context context, Activity activity, OrderTakenScreenFragment orderTakenScreenFragment) {
        this.gridImageList = gridImageList;
        this.context = context;
        this.activity = activity;
        this.morderTakenFragment = orderTakenScreenFragment;
        this.mfavouriteArrayList = favouriteArrayList;

    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemAdapter.ViewHolder holder, final int position) {
//        runEnterAnimation(holder.itemView, position);


        holder.list = gridImageList.get(position);
        holder.mName.setText(holder.list.getItem_name());
        holder.mDescription.setText(holder.list.getDescription());
        holder.mPrice.setText("$" + holder.list.getPrice());
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
        boolean isInteger = AdditionalClass.isInteger(holder.list.getPrice());
        final int priceList;
        final int[] itemCount = {0};
        if (holder.list.getQty() != 0) {
            int qty = holder.list.getQty();
            itemCount[0] = qty;
        }

        if (isInteger) {
            Log.e("check integer", holder.list.getPrice() + " is an integer");
            priceList = Integer.parseInt(holder.list.getPrice().trim());
        } else {
            Log.e("check integer", holder.list.getPrice() + " is not an integer");

            priceList = (int) Double.parseDouble(holder.list.getPrice().trim());
        }


        holder.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCount[0] = itemCount[0] + 1;
                if (itemCount[0] >= 0) {
                    holder.txtNumQty.setText(String.valueOf(itemCount[0]));
                    int totalPrice = priceList * itemCount[0];
                    holder.txtTotalPrice.setText("Rs." + String.valueOf(totalPrice));
                    holder.list.setQty(itemCount[0]);

                    if (listener != null) {
                        listener.onAddClick(itemCount[0], holder.list);
                    }


                }
            }
        });
        holder.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCount[0] = itemCount[0] - 1;
                if (itemCount[0] >= 0) {
                    holder.txtNumQty.setText(String.valueOf(itemCount[0]));
                    int totalPrice = priceList * itemCount[0];
                    holder.txtTotalPrice.setText("Rs." + String.valueOf(totalPrice));
                    holder.list.setQty(itemCount[0]);

                    if (listener != null) {
                        listener.onAddClick(itemCount[0], holder.list);
                    }

                }

            }
        });
        holder.txtTotalPrice.setText("Rs.0.0");
        if (holder.list.getQty() != 0) {
            holder.txtNumQty.setText(String.valueOf(holder.list.getQty()));
            int totalPrice = priceList * holder.list.getQty();
            holder.txtTotalPrice.setText("Rs." + String.valueOf(totalPrice));
        } else {
            holder.txtNumQty.setText(String.valueOf(itemCount[0]));

        }

        holder.imgNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.list = gridImageList.get(position);
                openDialog(holder.list);
            }
        });

        holder.selected_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialoge(position,holder.list,holder);
            }
        });

    }
    private void openDialoge(final int position, final ItemListRequestAndResponseModel.item_list list, ViewHolder holder) {

        AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();


        View alertLayout = inflater.inflate(R.layout.open_image_dialoge, null);
        ImageView imageClose = (ImageView) alertLayout.findViewById(R.id.imgClose);
        ImageView imageView_selected_image = (ImageView) alertLayout.findViewById(R.id.selected_image_dialoge);
        TextView description  = (TextView) alertLayout.findViewById(R.id.image_description);
        TextView selected_food_price = (TextView) alertLayout.findViewById(R.id.selected_item_price);
         favouriate_iamge = (ImageView) alertLayout.findViewById(R.id.favorite_image_click);

        Picasso.with(context)
                .load(holder.list.getImage()) // image url goes here
                .placeholder(R.drawable.cook8)
                .into(imageView_selected_image);
        description.setText(" Description : "+ holder.list.getDescription());
        selected_food_price.setText(" RS : "+ holder.list.getPrice());

        alertbox.setView(alertLayout);
        final AlertDialog alert = alertbox.create();

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = alert.getWindow();

        alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        layoutParams.copyFrom(window.getAttributes());

        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
      // alert.getWindow().setAttributes(layoutParams);
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        alert.show();


        ItemListRequestAndResponseModel.item_list item_list = gridImageList.get(position);
        if (item_list.isFavorite()) {
            favouriate_iamge.setImageResource(R.mipmap.ic_like_heart);
        }else {
            favouriate_iamge.setImageResource(R.mipmap.ic_unclick_heart);
        }

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        favouriate_iamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String getSpecificItemId = list.getItem_id();
                morderTakenFragment.addFavouriteApi(getSpecificItemId,position);
            }
        });

    }

    public void currentChangeFavouriteIcon(){
        favouriate_iamge.setImageResource(R.mipmap.ic_like_heart);
    }

    @Override
    public int getItemCount() {
        return gridImageList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mName,mDescription,mPrice,txtNumQty,txtTotalPrice;
        LinearLayout selected_item;

        ItemListRequestAndResponseModel.item_list list;
        ImageView img,favoriteImg,imgMinus,imgAdd,imgNotes;


        ViewHolder(View view) {
            super(view);
            favoriteImg=(ImageView)view.findViewById(R.id.favorite);
            mName=(TextView)view.findViewById(R.id.name);
            mDescription=(TextView)view.findViewById(R.id.description);
            mPrice=(TextView)view.findViewById(R.id.price);
            img=(ImageView)view.findViewById(R.id.image);
            imgMinus = (ImageView) view.findViewById(R.id.imgMinus);
            imgAdd = (ImageView) view.findViewById(R.id.imgAdd);
            txtNumQty = (TextView) view.findViewById(R.id.txtNumQty);
            txtTotalPrice = (TextView) view.findViewById(R.id.txtTotalPrice);
            imgNotes = (ImageView) view.findViewById(R.id.imgNotes);
            selected_item = (LinearLayout)view.findViewById(R.id.selected_item);

    }

    }

    private ItemListener listener;
    public void setListener(ItemListener listener)    {
        this.listener = listener;
    }



    private void openDialog(final ItemListRequestAndResponseModel.item_list item_list) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Notes");
        alertDialog.setMessage("Enter Your Notes");

        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        if (item_list.getNotes() != null) {
            if (!item_list.getNotes().isEmpty())
                input.setText(item_list.getNotes());
        }
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        item_list.setNotes(input.getText().toString());
                        if (listener != null) {
                            listener.onAddClick(item_list.getQty(), item_list);
                        }

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



}
