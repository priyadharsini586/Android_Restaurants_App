package com.nickteck.restaurantapp.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nickteck.restaurantapp.Adapter.ItemAdapter;
import com.nickteck.restaurantapp.Adapter.MyOrdersAdapter;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.additional_class.AdditionalClass;
import com.nickteck.restaurantapp.additional_class.RecyclerTouchListener;
import com.nickteck.restaurantapp.api.ApiClient;
import com.nickteck.restaurantapp.api.ApiInterface;
import com.nickteck.restaurantapp.model.AddWhislist;
import com.nickteck.restaurantapp.model.Constants;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.nickteck.restaurantapp.model.ItemModel;
import com.nickteck.restaurantapp.network.ConnectivityReceiver;
import com.nickteck.restaurantapp.network.MyApplication;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nickteck.restaurantapp.model.Constants.ITEM_BASE_URL;

public class ItemFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {
    RecyclerView recyclerView;
    boolean isNetworkConnected;
    ApiInterface apiInterface;
    ItemAdapter itemAdapter;
    ImageView image,imgClose;
    TextView name,description,price;
    private  ArrayList<ItemListRequestAndResponseModel.item_list> gridImageList=new ArrayList<>();
    View mainView;
    String itemId  ;
    int itemCount = 1;
    TextView txtNumQty,txtTotalPrice;
    AddWhislist favorite;

    TextView txtBrodgeIcon;
    public static ArrayList<ItemListRequestAndResponseModel.item_list> itemList = new ArrayList<>();
    ItemModel itemModel = ItemModel.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView =  inflater.inflate(R.layout.activity_item, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView tootBarTextViewb = (TextView)toolbar.findViewById(R.id.txtHomeToolBar);
        String content_text = getResources().getString(R.string.item_fragment);
        tootBarTextViewb.setText(content_text);

        txtBrodgeIcon = (TextView)toolbar.findViewById(R.id.txtBrodgeIcon);
        txtBrodgeIcon.setVisibility(View.GONE);


        if (itemModel.getListArrayList().size() == 0)
        {
            txtBrodgeIcon.setVisibility(View.GONE);
        }else
        {
            txtBrodgeIcon.setVisibility(View.VISIBLE);
            txtBrodgeIcon.setText(String.valueOf(itemModel.getListArrayList().size()));
        }

        itemId=getArguments().getString("listData");
        Log.e("item id",itemId);

        recyclerView=(RecyclerView)mainView.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {



                openDialognotification(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        checkConnection();
        MyApplication.getInstance().setConnectivityListener(this);
        return mainView;
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Toast.makeText(getActivity(), "Network not available", Toast.LENGTH_LONG).show();
            isNetworkConnected = false;
        }else
        {
            isNetworkConnected = true;
            getItemView();

        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {


//        if (isNetworkConnected != isConnected) {
            if (isConnected) {
                Toast.makeText(getActivity(), "Network Connected", Toast.LENGTH_LONG).show();
                getItemView();
            } else {
                Toast.makeText(getActivity(), "Network not available", Toast.LENGTH_LONG).show();
                AdditionalClass.showSnackBar(getActivity());

            }
//        }
        isNetworkConnected = isConnected;

    }

    public void getItemView()
    {
        if (isNetworkConnected)
        {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            JSONObject getJsonObject = new JSONObject();
            try {
                getJsonObject.put("cat_id",itemId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Call<ItemListRequestAndResponseModel> getCatageoryList = apiInterface.getItemList(getJsonObject);
            getCatageoryList.enqueue(new Callback<ItemListRequestAndResponseModel>() {
                @Override
                public void onResponse(Call<ItemListRequestAndResponseModel> call, Response<ItemListRequestAndResponseModel> response) {
                    if (response.isSuccessful())
                    {
                        ItemListRequestAndResponseModel itemListRequestAndResponseModel = response.body();
                        if (itemListRequestAndResponseModel.getStatus_code().equals(Constants.Success))
                        {
                            gridImageList = new ArrayList<ItemListRequestAndResponseModel.item_list>();
                            List<ItemListRequestAndResponseModel.item_list> getItemDetils = itemListRequestAndResponseModel.getItem_list();
                            for (int i = 0; i < getItemDetils.size(); i++) {
                                ItemListRequestAndResponseModel.item_list items=getItemDetils.get(i);
                                items.setItem_name(items.getItem_name());
                                items.setDescription(items.getDescription());
                                items.setPrice(items.getPrice());
                                items.setImage(items.getImage());
                                String url=ITEM_BASE_URL+items.getImage();
                                Log.e("url",url);
                                items.setFavourite("0");
                                items.setImage(url);
                                gridImageList.add(items);

                            }
                            itemAdapter=new ItemAdapter(gridImageList,getActivity());
                            recyclerView.setAdapter(itemAdapter);
                            itemAdapter.notifyDataSetChanged();

                        }else if (itemListRequestAndResponseModel.getStatus_code().equals(Constants.Failure))
                        {
                            Toast.makeText(getActivity(),itemListRequestAndResponseModel.getStatus_message(),Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ItemListRequestAndResponseModel> call, Throwable t) {

                }
            });
        }
    }
    private void openDialognotification(final int position) {
        final ItemListRequestAndResponseModel.item_list popitem=gridImageList.get(position);
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
         name=(TextView)dialog.findViewById(R.id.name) ;
         description=(TextView)dialog.findViewById(R.id.description) ;
         price=(TextView)dialog.findViewById(R.id.price) ;
         name.setText(popitem.getItem_name());
         description.setText(popitem.getDescription());
         price.setText("$"+popitem.getPrice());
         image=(ImageView)dialog.findViewById(R.id.image);
         Picasso.with(getActivity())
                .load(popitem.getImage()) // thumbnail url goes here
                .placeholder(R.drawable.cook8)
                .into(image, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.with(getActivity())
                                .load(popitem.getImage()) // image url goes here
                                .placeholder(R.drawable.cook8)
                                .into(image);
                    }

                    @Override
                    public void onError() {
                    }
                });
        imgClose = (ImageView) dialog.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 dialog.cancel();
             }
         });
        txtNumQty = (TextView) dialog.findViewById(R.id.txtNumQty);
        txtTotalPrice = (TextView) dialog.findViewById(R.id.txtTotalPrice);
        ImageView imgAdd = (ImageView) dialog.findViewById(R.id.imgAdd);
        LinearLayout wishlist=(LinearLayout)dialog.findViewById(R.id.add_wishlist);
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popitem.getItem_id();
                getWishList(popitem,position);
            }
        });

        boolean isInteger = AdditionalClass.isInteger(popitem.getPrice());
        final int priceList ;
        if (isInteger) {
            Log.e("check integer",popitem.getPrice()+" is an integer");
            priceList = Integer.parseInt(popitem.getPrice().trim());
        } else {
            Log.e("check integer",popitem.getPrice()+" is not an integer");

            priceList = (int) Double.parseDouble(popitem.getPrice().trim());
        }
        itemCount = 1;
         imgAdd.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                  itemCount = itemCount + 1;
                 if (itemCount >= 1)
                 {
                     txtNumQty.setText(String.valueOf(itemCount));
                     int totalPrice = priceList * itemCount;
                     txtTotalPrice.setText("$"+String.valueOf(totalPrice));
                 }
             }
         });
         ImageView imgMinus = (ImageView) dialog.findViewById(R.id.imgMinus);
         imgMinus.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 itemCount = itemCount - 1;
                 if (itemCount >= 1)
                 {
                     txtNumQty.setText(String.valueOf(itemCount));
                     int totalPrice =priceList * itemCount;
                     txtTotalPrice.setText("$"+String.valueOf(totalPrice));
                 }

             }
         });
         txtNumQty.setText( String.valueOf(itemCount));
         txtTotalPrice.setText("$"+popitem.getPrice());



        LinearLayout addToCart = (LinearLayout) dialog.findViewById(R.id.addToCart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isHave = false;
                if (itemList.size()!=0) {
                    for (int j = 0; j < itemList.size(); j++) {
                        ItemListRequestAndResponseModel.item_list item_list = itemList.get(j);
                        if (item_list.getItem_id().equals(popitem.getItem_id())) {
                            Log.e("check already", "already have");
                            isHave = true;

                        }
                    }

                    if (!isHave)
                    {
                        txtBrodgeIcon.setVisibility(View.VISIBLE);
                        popitem.setQty(itemCount);
                        itemList.add(popitem);
                        itemModel.setListArrayList(itemList);
                        txtBrodgeIcon.setText(String.valueOf(itemModel.getListArrayList().size()));
                        Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();
                    }else
                    {
                        Toast.makeText(getActivity(), "Already Added to Cart", Toast.LENGTH_LONG).show();

                    }
                }else
                {
                    txtBrodgeIcon.setVisibility(View.VISIBLE);
                    popitem.setQty(itemCount);
                    itemList.add(popitem);
                    itemModel.setListArrayList(itemList);
                    txtBrodgeIcon.setText(String.valueOf(itemModel.getListArrayList().size()));
                    Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();
                }



            }
        });

         description.measure(0,0);
        image.measure(0,0);

        float density =getContext().getResources().getDisplayMetrics().density;
        float densityHeight = image.getMeasuredHeight() / density;

        ViewTreeObserver vto = description.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                description.getViewTreeObserver().removeOnPreDrawListener(this);
               int  finalHeight = description.getMeasuredHeight() + 250 + 260;
                int finalWidth = description.getMeasuredWidth();
                Log.e("height", String.valueOf( "Height: " + finalHeight +  " Width: " + finalWidth));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, finalHeight  );

                return true;
            }
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        dialog.show();
    }
    public void  getWishList(final ItemListRequestAndResponseModel.item_list item_list, final int position)
    {
        if (isNetworkConnected)
        {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            JSONObject getJsonObject = new JSONObject();
            try {
                getJsonObject.put("customer_id",1);
                getJsonObject.put("item_id",25);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Call<AddWhislist> getCatageoryList = apiInterface.getWhishlist(getJsonObject);
            getCatageoryList.enqueue(new Callback<AddWhislist>() {
                @Override
                public void onResponse(Call<AddWhislist> call, Response<AddWhislist> response) {
                    if (response.isSuccessful())
                    {
                        AddWhislist addWhislist = response.body();

                        if (addWhislist.getStatus_code().equals(Constants.Success))
                        {
                            item_list.setFavourite("1");
                            gridImageList.set(position,item_list);
                            Toast.makeText(getActivity(),addWhislist.getStatus_message(),Toast.LENGTH_LONG).show();
                            itemAdapter.notifyItemChanged(position);

                        }else if (addWhislist.getStatus_code().equals(Constants.Failure))
                        {

                            Toast.makeText(getActivity(),addWhislist.getStatus_message(),Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddWhislist> call, Throwable t) {

                }


            });

        }
    }
}
