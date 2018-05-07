package com.nickteck.restaurantapp.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SyncRequest;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nickteck.restaurantapp.Adapter.MyOrdersAdapter;
import com.nickteck.restaurantapp.Db.Database;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.activity.MenuNavigationActivity;
import com.nickteck.restaurantapp.additional_class.AdditionalClass;
import com.nickteck.restaurantapp.chat.GetFromDesktopListener;
import com.nickteck.restaurantapp.chat.rabbitmq_server.RabbitmqServer;
import com.nickteck.restaurantapp.model.Constants;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.nickteck.restaurantapp.model.ItemModel;
import com.nickteck.restaurantapp.network.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ng.max.slideview.SlideView;


public class MyOrdersFragment extends Fragment implements MyOrdersAdapter.Callback,GetFromDesktopListener {

    View mainView;
    TextView txtBrodgeIcon;
    ItemModel itemModel;
    RecyclerView myOrderRecycleView;
    MyOrdersAdapter myOrdersAdapter;
    TextView txtTotalPrice,txtPlaceItem,txtUpdateItem;
    LinearLayout ldtPlaceOrder,ldtAddMore;
    RabbitmqServer rabbitmqServer;
    ArrayList<ItemListRequestAndResponseModel.item_list>itemLists;
    Database database ;
    Spinner cat_spinner;
    public String TAG = MyOrdersFragment.class.getName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView =  inflater.inflate(R.layout.fragment_my_orders, container, false);
        // Inflate the layout for this fragment
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView tootBarTextViewb = (TextView)toolbar.findViewById(R.id.txtHomeToolBar);
        String content_text = getResources().getString(R.string.my_order);
        tootBarTextViewb.setText(content_text);
        txtBrodgeIcon = (TextView)toolbar.findViewById(R.id.txtBrodgeIcon);
        itemModel = ItemModel.getInstance();
        if (itemModel.getListArrayList().size() == 0)
        {
            txtBrodgeIcon.setVisibility(View.GONE);
        }else
        {
            txtBrodgeIcon.setVisibility(View.VISIBLE);
            txtBrodgeIcon.setText(String.valueOf(itemModel.getListArrayList().size()));
        }
        cat_spinner = (Spinner)toolbar.findViewById(R.id.cat_spinner);
        cat_spinner.setVisibility(View.GONE);

        Log.e("itemList", String.valueOf(itemModel.getListArrayList().size()));
         database = new Database(getActivity());
        myOrderRecycleView = (RecyclerView) mainView.findViewById(R.id.myOrderRecycleView);
        itemLists = itemModel.getListArrayList();
        myOrdersAdapter=new MyOrdersAdapter(itemLists,getActivity());
        myOrderRecycleView.setAdapter(myOrdersAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        myOrderRecycleView.setLayoutManager(linearLayoutManager);
        myOrdersAdapter.notifyDataSetChanged();

        myOrdersAdapter.setListener(MyOrdersFragment.this);
        txtTotalPrice = (TextView) mainView.findViewById(R.id.txtTotalPrice);
        double price = 0;
        for (int i=0;i<itemModel.getListArrayList().size();i++)
        {
            ItemListRequestAndResponseModel.item_list item_list = itemModel.getListArrayList().get(i);
            double getPrice = Double.parseDouble(item_list.getPrice());
            double qty = item_list.getQty();
            getPrice = getPrice * qty;
//            double priceGet = item_list.getQty() * item_list.getPrice();
            price = price + getPrice;
        }
//        txtTotalPrice.setText(String.valueOf(price));

        txtTotalPrice.setText("Total : "+String.valueOf(price));

        txtUpdateItem = (TextView) mainView.findViewById(R.id.txtUpdateItem);
        txtPlaceItem = (TextView) mainView.findViewById(R.id.txtPlaceItem);
        if (itemModel.isAlreadyPlace())
        {
            txtPlaceItem.setVisibility(View.GONE);
            txtUpdateItem.setVisibility(View.VISIBLE);
        }else
        {
            txtPlaceItem.setVisibility(View.VISIBLE);
            txtUpdateItem.setVisibility(View.GONE);
        }
        ldtPlaceOrder = (LinearLayout) mainView.findViewById(R.id.ldtPlaceOrder);
        ldtPlaceOrder.setVisibility(View.GONE);
        ldtPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Conformation?")
                        .setMessage("Do you want to Place this item?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                sendToDesktop();
                                itemModel.setAlreadyPlace(true);
                                txtPlaceItem.setVisibility(View.GONE);
                                txtUpdateItem.setVisibility(View.VISIBLE);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
        MyApplication.getInstance().setGetFromDesktopListener(this);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
        {
            new RabbitmqServer().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else {
            new RabbitmqServer().execute();
        }
        ldtAddMore = (LinearLayout) mainView.findViewById(R.id.ldtAddMore);
        ldtAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderTakenScreenFragment catagoryFragment = new OrderTakenScreenFragment();
                AdditionalClass.replaceFragment(catagoryFragment, Constants.ORDER_TAKEN_FRAGMENT,(AppCompatActivity)getActivity());
            }
        });
        SlideView slideView = (SlideView) mainView.findViewById(R.id.slideView);
        slideView.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                // vibrate the device
                Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(500);

                new AlertDialog.Builder(getActivity())
                        .setTitle("Conformation?")
                        .setMessage("Do you want to Place this item?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

//
                                Log.e(TAG, "onClick: "+ itemModel.isAlreadyPlace());
                                if (!itemModel.isAlreadyPlace())
                                {
                                    sendToDesktop();
                                    itemModel.setAlreadyPlace(true);
                                    txtPlaceItem.setVisibility(View.GONE);
                                    txtUpdateItem.setVisibility(View.VISIBLE);
                                    removeItemFromOrderItem();
                                }else
                                {
                                    Toast.makeText(getActivity(),"Already send to Desktop",Toast.LENGTH_LONG).show();
                                }


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

            }
        });

//        new RabbitmqServer().execute();
        return mainView;
    }


    public void removeItemFromOrderItem()
    {
       /* ItemModel itemModel = ItemModel.getInstance();
        itemModel.getListArrayList().clear();*/
        itemLists.clear();
        myOrdersAdapter.notifyDataSetChanged();

        itemModel = ItemModel.getInstance();
        if (itemModel.getListArrayList().size() == 0)
        {
            txtBrodgeIcon.setVisibility(View.GONE);
        }else
        {
            txtBrodgeIcon.setVisibility(View.VISIBLE);
            txtBrodgeIcon.setText(String.valueOf(itemModel.getListArrayList().size()));
        }
        txtTotalPrice.setText("Total : 0.0");
    }
    @Override
    public void onChangeItemCount(int totaltcount) {


        double totlaPrice = 0.0;
        for (int i=0;i<itemModel.getListArrayList().size();i++)
        {
            ItemListRequestAndResponseModel.item_list item_list = itemModel.getListArrayList().get(i);
            double qty = (double) item_list.getQty();
            double price = Double.parseDouble(item_list.getPrice());
            price = qty *price;
            totlaPrice = totlaPrice + price;
            Log.e("price",String.valueOf(totlaPrice));
        }
        txtTotalPrice.setText("Total : "+String.valueOf(totlaPrice));
        if (totaltcount == 0)
        {
            txtBrodgeIcon.setVisibility(View.GONE);
        }else
        {
            txtBrodgeIcon.setVisibility(View.VISIBLE);
            txtBrodgeIcon.setText(String.valueOf(itemModel.getListArrayList().size()));
        }

    }

    @Override
    public void itemIncreased(double count) {

        double totlaPrice = 0.0;
        for (int i=0;i<itemModel.getListArrayList().size();i++)
        {
            ItemListRequestAndResponseModel.item_list item_list = itemModel.getListArrayList().get(i);
            double qty = (double) item_list.getQty();
            double price = Double.parseDouble(item_list.getPrice());
            price = qty *price;
            totlaPrice = totlaPrice + price;
            Log.e("price",String.valueOf(totlaPrice));
        }
        txtTotalPrice.setText("Total : "+String.valueOf(totlaPrice));
    }

    @Override
    public void itemDecreased(double count) {
        double totlaPrice = 0.0;
        for (int i=0;i<itemModel.getListArrayList().size();i++)
        {
            ItemListRequestAndResponseModel.item_list item_list = itemModel.getListArrayList().get(i);
            double qty = (double) item_list.getQty();
            double price = Double.parseDouble(item_list.getPrice());
            price = qty *price;
            totlaPrice = totlaPrice + price;
            Log.e("price",String.valueOf(totlaPrice));
        }
        txtTotalPrice.setText("Total : "+String.valueOf(totlaPrice));
    }


    public void sendToDesktop()
    {
        String message;
        JSONObject json = new JSONObject();

        try {
            json.put("table", database.getData());
            json.put("from", "mobile");
            json.put("cus_id",database.getCustomerId());
            JSONArray itemArray = new JSONArray();
            for (int i=0;i<itemModel.getListArrayList().size();i++)
            {
                ItemListRequestAndResponseModel.item_list  item_list = itemModel.getListArrayList().get(i);
                JSONObject item = new JSONObject();
                item.put("item_name",item_list.getItem_name());
                item.put("qty",item_list.getQty());
                item.put("item_id",item_list.getItem_id());
                item.put("price",item_list.getPrice());
                item.put("short_code",item_list.getShort_code());
                if (item_list.getNotes() == null)
                {
                    item.put("notes","notes");
                }else {
                item.put("notes",item_list.getNotes());
                }
                itemArray.put(item);
            }
            json.put("Item_list", itemArray);

            message = json.toString();
            rabbitmqServer = new RabbitmqServer();
            rabbitmqServer.sendMsg(message);

        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

    @Override
    public void getFromDeskTop(String result) {

        String table = database.getData();
        Log.e("list",result);
        try {
            JSONObject getResult = new JSONObject(result);
            if (getResult.has("from"))
            {
                if (getResult.getString("from").equals("Desktop"))
                {
                    Log.e("result",getResult.getString("table"));
                    final ItemModel itemModel = ItemModel.getInstance();
                    final ArrayList<ItemListRequestAndResponseModel.item_list> savetoModel = new ArrayList<>();
                    if (getResult.getString("table").equals(database.getData())) {
                        JSONArray getListArray = getResult.getJSONArray("Item_list");
                        if (getListArray.length() != 0) {
                            itemLists.clear();
                            final ArrayList<ItemListRequestAndResponseModel.item_list> item_listArrayList = new ArrayList<>();
                            for (int j = 0; j < getListArray.length(); j++) {
                                JSONObject jsonObject = getListArray.getJSONObject(j);
                                final ItemListRequestAndResponseModel.item_list item_list = new ItemListRequestAndResponseModel.item_list();
                                item_list.setShort_code(jsonObject.getString("short_code"));
                                item_list.setQty(jsonObject.getInt("qty"));
                                item_list.setItem_name(jsonObject.getString("item_name"));
                                item_list.setDescription(jsonObject.getString("des"));
                                item_list.setImage(jsonObject.getString("image"));
                                item_list.setPrice(jsonObject.getString("price"));
                                item_list.setNotes(jsonObject.getString("notes"));
                                item_list.setItem_id(jsonObject.getString("item_id"));
                                item_listArrayList.add(item_list);
                                savetoModel.add(item_list);
                                itemModel.setListArrayList(savetoModel);
                                itemLists.add(item_list);

                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myOrdersAdapter.notifyDataSetChanged();
                                    setTotalPrice();
                                    Log.e("list size", String.valueOf(itemLists.size()));
                                }
                            });


                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    itemLists.clear();
                                    myOrdersAdapter.notifyDataSetChanged();
                                    txtTotalPrice.setText("Total : 0.0");
                                }
                            });
                        }

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setTotalPrice()
    {
        double totlaPrice = 0.0;
        for (int i=0;i<itemLists.size();i++)
        {
            ItemListRequestAndResponseModel.item_list item_list = itemLists.get(i);
            double qty = (double) item_list.getQty();
            double price = Double.parseDouble(item_list.getPrice());
            price = qty *price;
            totlaPrice = totlaPrice + price;
            Log.e("price",String.valueOf(totlaPrice));
        }
        txtTotalPrice.setText("Total : "+String.valueOf(totlaPrice));
        ItemModel itemModel = ItemModel.getInstance();
        if (itemModel.getListArrayList().size() == 0)
        {
            txtBrodgeIcon.setVisibility(View.GONE);
        }else
        {
            txtBrodgeIcon.setVisibility(View.VISIBLE);
            txtBrodgeIcon.setText(String.valueOf(itemModel.getListArrayList().size()));
        }


    }
}
