package com.nickteck.restaurantapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nickteck.restaurantapp.Adapter.CatagoryAdapter;
import com.nickteck.restaurantapp.Adapter.CustomSubCatGridViewAdapter;
import com.nickteck.restaurantapp.Adapter.ItemAdapter;
import com.nickteck.restaurantapp.Adapter.VarietyAdapter;
import com.nickteck.restaurantapp.Db.Database;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.activity.RatingActivity;
import com.nickteck.restaurantapp.activity.TableActivity;
import com.nickteck.restaurantapp.additional_class.AdditionalClass;
import com.nickteck.restaurantapp.additional_class.RecyclerTouchListener;
import com.nickteck.restaurantapp.api.ApiClient;
import com.nickteck.restaurantapp.api.ApiInterface;
import com.nickteck.restaurantapp.interfaceFol.ItemListener;
import com.nickteck.restaurantapp.model.Constants;
import com.nickteck.restaurantapp.model.FavouriteListData;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.nickteck.restaurantapp.model.ItemModel;
import com.nickteck.restaurantapp.model.LoginRequestAndResponse;
import com.nickteck.restaurantapp.model.RatingResponseModel;
import com.nickteck.restaurantapp.network.ConnectivityReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nickteck.restaurantapp.model.Constants.CATEGORY_BASE_URL;
import static com.nickteck.restaurantapp.model.Constants.ITEM_BASE_URL;
import static com.nickteck.restaurantapp.model.Constants.SUB_CATEGORY_BASE_URL;


public class OrderTakenScreenFragment extends Fragment implements ItemListener,ConnectivityReceiver.ConnectivityReceiverListener{
    View view;
    ApiInterface apiInterface;
    RecyclerView variety_recycler_view,item_recycler_view,sub_cat_recycler_view,cat_recycler_view;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<ItemListRequestAndResponseModel.cat_list> subCatList,tempSubcatList,catList;
    ArrayList<ItemListRequestAndResponseModel.Variety_id_list> varietyIdLists;
    TextView txtBrodgeIcon;
    CatagoryAdapter gridAdapter,catagoryAdapter;
    CustomSubCatGridViewAdapter customSubCatGridViewAdapter;
    GridView grid_view_image_text;
    VarietyAdapter varietyAdapter;
    ItemAdapter itemAdapter;
    String cat_id;
    HashMap<String,ArrayList<ItemListRequestAndResponseModel.item_list>> getVarityList = new HashMap<>();
    HashMap<String,ItemListRequestAndResponseModel.Variety_id_list> hashvarityList = new HashMap<>();
    private  ArrayList<ItemListRequestAndResponseModel.item_list> gridImageList,tempItemList;
    public static ArrayList<ItemListRequestAndResponseModel.item_list> itemList = new ArrayList<>();
    ItemModel itemModel = ItemModel.getInstance();
    TextView txtTotalPrice;
    LinearLayout ldtPlaceOrder,ldtList;
    private boolean isSpinnerTouched = false;
    boolean isNetworkConnected;
    Database database;
    ArrayList<FavouriteListData.FavouriteListDetails> favouriteListDetails_adapter;
    private ProgressBar progress_ratings;
    private ArrayList<String> itemIdList = new ArrayList();
    private String localItemId;
    LayoutAnimationController controller;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_taken_screen, container, false);
        /*subCatagory=(RecyclerView)view.findViewById(R.id.recycler_view);
        subCatagory.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), subCatagory, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                ItemListRequestAndResponseModel.cat_list list = subCatList.get(position);
                getVarityList = new HashMap<>();
                cat_id = list.getSub_Cat_id();
                getItemView(list.getSub_Cat_id());

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));*/


        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView tootBarTextViewb = (TextView)toolbar.findViewById(R.id.txtHomeToolBar);
        String content_text = getResources().getString(R.string.category_fragment);
        tootBarTextViewb.setText(content_text);

        txtBrodgeIcon = (TextView)toolbar.findViewById(R.id.txtBrodgeIcon);
        txtBrodgeIcon.setVisibility(View.GONE);
        ItemModel itemModel = ItemModel.getInstance();
        if (itemModel.getListArrayList().size() == 0)
        {
            txtBrodgeIcon.setVisibility(View.GONE);
        }else
        {
            txtBrodgeIcon.setVisibility(View.VISIBLE);
            txtBrodgeIcon.setText(String.valueOf(itemModel.getListArrayList().size()));
        }

        ldtList = (LinearLayout)view.findViewById(R.id.ldtList);
        txtTotalPrice = (TextView) view.findViewById(R.id.txtTotalPrice);
        ldtPlaceOrder = (LinearLayout) view.findViewById(R.id.ldtPlaceOrder);
        progress_ratings = (ProgressBar) view.findViewById(R.id.progress_ratings);
        ldtPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrdersFragment myOrdersFragment = new MyOrdersFragment();
                AdditionalClass.replaceFragment(myOrdersFragment,Constants.MY_ORDERS_FRAGMENT,(AppCompatActivity)getActivity());
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // cancle the Visual indication of a refresh
                        mSwipeRefreshLayout.setRefreshing(true);
                        getSubCategoryData();
                    }
                }, 3000);



            }
        });

        variety_recycler_view = (RecyclerView) view.findViewById(R.id.variety_recycler_view);
        variety_recycler_view.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), variety_recycler_view, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
               /* if (position != 0) {
                    ItemListRequestAndResponseModel.Variety_id_list varietyIdList = varietyIdLists.get(position);

                    if (getVarityList.size() != 0) {
                        gridImageList.clear();
                        for (int i = 0; i < getVarityList.get(varietyIdList.getVariety_id()).size(); i++) {
                            gridImageList.add(getVarityList.get(varietyIdList.getVariety_id()).get(i));
                        }
                        itemAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "Please select category", Toast.LENGTH_LONG).show();
                    }
                }else
                {


                    getItemView(cat_id);

                }*/

                ItemListRequestAndResponseModel.Variety_id_list varietyIdList = varietyIdLists.get(position);

                if (getVarityList.size() != 0) {
                    gridImageList.clear();
                    for (int i = 0; i < getVarityList.get(varietyIdList.getVariety_id()).size(); i++) {
                        gridImageList.add(getVarityList.get(varietyIdList.getVariety_id()).get(i));
                    }
                    itemAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Please select category", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        sub_cat_recycler_view = (RecyclerView) view.findViewById(R.id.sub_cat_recycler_view);
        cat_recycler_view = (RecyclerView) view.findViewById(R.id.cat_recycler_view);
        cat_recycler_view.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), cat_recycler_view, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                ItemListRequestAndResponseModel.cat_list  cat_list = catList.get(position);
                subCatList.clear();
                subCatList.addAll(tempSubcatList);
                gridImageList.clear();
                gridImageList.addAll(tempItemList);
                searchItemBasedOnCat(cat_list.getCat_id());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        sub_cat_recycler_view.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), sub_cat_recycler_view, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ItemListRequestAndResponseModel.cat_list list = subCatList.get(position);
                getVarityList = new HashMap<>();
                cat_id = list.getSub_Cat_id();
                getItemView(list.getSub_Cat_id());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        changePrice();
        item_recycler_view = (RecyclerView) view.findViewById(R.id.item_recycler_view);

        mSwipeRefreshLayout.setRefreshing(true);
        getCatagoryData();
        getSubCategoryData();
        getVarietyData();
        // get Favourite list for to check the item id in adapter
        getFavouriteListApi();
        return view;
    }




    private void getCatagoryData() {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ItemListRequestAndResponseModel> getCatageoryList = apiInterface.getCatagoryList();
        getCatageoryList.enqueue(new Callback<ItemListRequestAndResponseModel>() {
            @Override
            public void onResponse(Call<ItemListRequestAndResponseModel> call, Response<ItemListRequestAndResponseModel> response) {
                if (response.isSuccessful())
                {
                    mSwipeRefreshLayout.setRefreshing(false);
                    ItemListRequestAndResponseModel itemListRequestAndResponseModel = response.body();
                    if (itemListRequestAndResponseModel.getStatus_code().equals(Constants.Success))
                    {
                        catList = new ArrayList<>();

                        ArrayList getItemDetils = itemListRequestAndResponseModel.getCat_list();
                        for (int i = 0; i < getItemDetils.size(); i++) {
                            ItemListRequestAndResponseModel.cat_list  categoryList = (ItemListRequestAndResponseModel.cat_list) getItemDetils.get(i);
                            categoryList.setCat_name(categoryList.getCat_name());
                            String url=CATEGORY_BASE_URL+categoryList.getImage();
                            categoryList.setCat_id(categoryList.getCat_id());
                            categoryList.setImage(url);
                            catList.add(categoryList);

                        }
                        catagoryAdapter=new CatagoryAdapter(getActivity(),catList);
                        cat_recycler_view.setAdapter(catagoryAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                        cat_recycler_view.setLayoutManager(linearLayoutManager);


                        cat_recycler_view.setLayoutAnimation(controller);
                        cat_recycler_view.getAdapter().notifyDataSetChanged();
                        cat_recycler_view.scheduleLayoutAnimation();
                    }
                }
            }

            @Override
            public void onFailure(Call<ItemListRequestAndResponseModel> call, Throwable t) {

            }
        });
    }

    private void getItemList() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ItemListRequestAndResponseModel> getCatageoryList = apiInterface.getItemData();
        getCatageoryList.enqueue(new Callback<ItemListRequestAndResponseModel>() {
            @Override
            public void onResponse(Call<ItemListRequestAndResponseModel> call, Response<ItemListRequestAndResponseModel> response) {
                if (response.isSuccessful())
                {
                    ItemListRequestAndResponseModel itemListRequestAndResponseModel = response.body();
                    if (itemListRequestAndResponseModel.getStatus_code().equals(Constants.Success))
                    {
                        gridImageList = new ArrayList<ItemListRequestAndResponseModel.item_list>();
                        tempItemList = new ArrayList<ItemListRequestAndResponseModel.item_list>();
                        List<ItemListRequestAndResponseModel.item_list> getItemDetils = itemListRequestAndResponseModel.getItem_list();
                        for (int i = 0; i < getItemDetils.size(); i++) {
                            ItemListRequestAndResponseModel.item_list items=getItemDetils.get(i);
                            items.setItem_name(items.getItem_name());
                            items.setDescription(items.getDescription());
                            items.setPrice(items.getPrice());
                            items.setImage(items.getImage());
                            if (itemIdList.contains(items.getItem_id()))
                                items.setFavorite(true);
                            String url=ITEM_BASE_URL+items.getImage();
                            Log.e("sub catagory url",url);
                            items.setFavourite("0");
                            items.setImage(url);
                            for (int j = 0 ; j < itemModel.getListArrayList().size();j++) {
                                ItemListRequestAndResponseModel.item_list item_list = itemModel.getListArrayList().get(j);
                                if (item_list.getItem_id().equals(items.getItem_id()))
                                {
                                    items.setQty(item_list.getQty());
                                    if (item_list.getNotes() != null)
                                    {
                                        items.setNotes(item_list.getNotes());
                                    }
                                    Log.e("qty", String.valueOf(items.getQty()));
                                }
                            }
                            gridImageList.add(items);


                        }
                        tempItemList.addAll(gridImageList);
                        itemAdapter=new ItemAdapter(gridImageList,favouriteListDetails_adapter,getActivity(),getActivity(),OrderTakenScreenFragment.this );
                        itemAdapter.setListener(OrderTakenScreenFragment.this);
                        item_recycler_view.setAdapter(itemAdapter);
                        try {
                            controller =
                                    AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_fall_down);

                        }catch (Exception e){
                            Log.d("ResourcesException","");
                        }
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                        item_recycler_view.setLayoutManager(linearLayoutManager);
                        item_recycler_view.setLayoutAnimation(controller);
                        item_recycler_view.getAdapter().notifyDataSetChanged();
                        item_recycler_view.scheduleLayoutAnimation();
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



    private void getVarietyData() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ItemListRequestAndResponseModel> getVarietyList = apiInterface.getVarietyList();
        getVarietyList.enqueue(new Callback<ItemListRequestAndResponseModel>() {
            @Override
            public void onResponse(Call<ItemListRequestAndResponseModel> call, Response<ItemListRequestAndResponseModel> response) {
                if (response.isSuccessful())
                {
                    ItemListRequestAndResponseModel itemListRequestAndResponseModel = response.body();


                    if (itemListRequestAndResponseModel.getStatusCode().equals(Constants.Success)) {
                        varietyIdLists = new ArrayList<>();
                        ArrayList getItemDetils = new ArrayList();
                        getItemDetils = itemListRequestAndResponseModel.getVariety_id_list();
                        /*ItemListRequestAndResponseModel.Variety_id_list model = new ItemListRequestAndResponseModel.Variety_id_list();
                        model.setVariety_name("All");
                        model.setVariety_id("0");
                        varietyIdLists.add(model);*/
                        for (int i = 0; i < getItemDetils.size(); i++) {

                            ItemListRequestAndResponseModel.Variety_id_list categoryList = (ItemListRequestAndResponseModel.Variety_id_list) getItemDetils.get(i);
                            varietyIdLists.add(categoryList);
                            hashvarityList.put(categoryList.getVariety_id(),categoryList);

                            Log.e("variety",categoryList.getVariety_name());
                        }


                        varietyAdapter=new VarietyAdapter(getActivity(),varietyIdLists);
                        variety_recycler_view.setAdapter(varietyAdapter);
//                        catagory.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL));
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                        variety_recycler_view.setLayoutManager(linearLayoutManager);
                        try {
                            controller =
                                    AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_fall_down);

                        }catch (Exception e){
                            Log.d("ResourcesException","");
                        }

                        variety_recycler_view.setLayoutAnimation(controller);
                        variety_recycler_view.getAdapter().notifyDataSetChanged();
                        variety_recycler_view.scheduleLayoutAnimation();
                        varietyAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ItemListRequestAndResponseModel> call, Throwable t) {

            }
        });

    }

    public void getSubCategoryData()
    {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ItemListRequestAndResponseModel> getCatageoryList = apiInterface.getSubCatagoryList();
        getCatageoryList.enqueue(new Callback<ItemListRequestAndResponseModel>() {
            @Override
            public void onResponse(Call<ItemListRequestAndResponseModel> call, Response<ItemListRequestAndResponseModel> response) {
                if (response.isSuccessful())
                {
                    mSwipeRefreshLayout.setRefreshing(false);
                    ItemListRequestAndResponseModel itemListRequestAndResponseModel = response.body();
                    if (itemListRequestAndResponseModel.getStatusCode().equals(Constants.Success))
                    {
                        subCatList = new ArrayList<>();
                        tempSubcatList = new ArrayList<>();
                        ArrayList getItemDetils = itemListRequestAndResponseModel.getCat_list();
                        for (int i = 0; i < getItemDetils.size(); i++) {
                            ItemListRequestAndResponseModel.cat_list  categoryList = (ItemListRequestAndResponseModel.cat_list) getItemDetils.get(i);
                            categoryList.setSub_Cat_name(categoryList.getSub_Cat_name());
                            String url=SUB_CATEGORY_BASE_URL+categoryList.getImage();
                            categoryList.setSub_Cat_id(categoryList.getSub_Cat_id());
                            categoryList.setImage(url);
                            subCatList.add(categoryList);

                        }
                        tempSubcatList.addAll(subCatList);
                         gridAdapter=new CatagoryAdapter(getActivity(),subCatList);
                        sub_cat_recycler_view.setAdapter(gridAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                        sub_cat_recycler_view.setLayoutManager(linearLayoutManager);
                        final LayoutAnimationController controller =
                                AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_fall_down);

                        sub_cat_recycler_view.setLayoutAnimation(controller);
                        sub_cat_recycler_view.getAdapter().notifyDataSetChanged();
                        sub_cat_recycler_view.scheduleLayoutAnimation();


                    }
                }
            }

            @Override
            public void onFailure(Call<ItemListRequestAndResponseModel> call, Throwable t) {

            }
        });
    }


    public void getItemView(String catId)
    {

            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            JSONObject getJsonObject = new JSONObject();
            try {
                getJsonObject.put("sub_cat_id",catId);
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
                        if (itemListRequestAndResponseModel.getStatus_code() != null) {

                            if (itemListRequestAndResponseModel.getStatus_code().equals(Constants.Success)) {
                                gridImageList = new ArrayList<ItemListRequestAndResponseModel.item_list>();
                                List<ItemListRequestAndResponseModel.item_list> getItemDetils = itemListRequestAndResponseModel.getItem_list();
                                for (int i = 0; i < getItemDetils.size(); i++) {
                                    ItemListRequestAndResponseModel.item_list items = getItemDetils.get(i);
                                    items.setItem_name(items.getItem_name());
                                    items.setDescription(items.getDescription());
                                    items.setPrice(items.getPrice());
                                    items.setImage(items.getImage());
                                    if (itemIdList.contains(items.getItem_id()))
                                        items.setFavorite(true);
                                    String url = ITEM_BASE_URL + items.getImage();
                                    Log.e("url", url);
                                    items.setFavourite("0");
                                    items.setImage(url);
                                    for (int j = 0; j < itemModel.getListArrayList().size(); j++) {
                                        ItemListRequestAndResponseModel.item_list item_list = itemModel.getListArrayList().get(j);
                                        if (item_list.getItem_id().equals(items.getItem_id())) {
                                            items.setQty(item_list.getQty());
                                            if (item_list.getNotes() != null) {
                                                items.setNotes(item_list.getNotes());
                                            }
                                            Log.e("qty", String.valueOf(items.getQty()));
                                        }
                                    }

                                    gridImageList.add(items);

                                    ArrayList<ItemListRequestAndResponseModel.Variety_id_list> varityList = items.getVariety_list();
                                    ArrayList<ItemListRequestAndResponseModel.item_list> itemVarityList = new ArrayList<>();
                                    if (varityList.size() != 0) {
                                        variety_recycler_view.setVisibility(View.VISIBLE);
                                        ArrayList<String> varityId = new ArrayList();

                                        for (int j = 0; j < varityList.size(); j++) {
                                            ItemListRequestAndResponseModel.Variety_id_list variety_id_list = varityList.get(j);

                                            varityId.add(variety_id_list.getVarietyid());
                                            itemVarityList.add(items);
                                            if (getVarityList.containsKey(variety_id_list.getVarietyid())) {
                                                ArrayList<ItemListRequestAndResponseModel.item_list> list = getVarityList.get(variety_id_list.getVarietyid());
                                                for (int k = 0; k < list.size(); k++) {
                                                    itemVarityList.add(list.get(k));
                                                }
                                                getVarityList.put(variety_id_list.getVarietyid(), itemVarityList);
                                                itemVarityList = new ArrayList<>();
                                            } else {
                                                getVarityList.put(variety_id_list.getVarietyid(), itemVarityList);
                                                itemVarityList = new ArrayList<>();
                                            }
                                        }

                                        ArrayList<ItemListRequestAndResponseModel.Variety_id_list> tempVarietyList = new ArrayList<>();
                                        for (int k = 0 ; k < varityId.size() ; k ++)
                                        {
                                            String id = varityId.get(k);
                                            hashvarityList.get(id);
                                            tempVarietyList.add(hashvarityList.get(id));
                                        }
                                        varietyIdLists.clear();
                                        varietyAdapter.clearView();
                                        varietyIdLists.addAll(tempVarietyList);
                                        varietyAdapter.notifyDataSetChanged();

                                    }else
                                    {
                                        variety_recycler_view.setVisibility(View.INVISIBLE);
                                    }
                                }
                                itemAdapter = new ItemAdapter(gridImageList,favouriteListDetails_adapter,getActivity(),getActivity(),OrderTakenScreenFragment.this);
                                itemAdapter.setListener(OrderTakenScreenFragment.this);
                                item_recycler_view.setAdapter(itemAdapter);
                                final LayoutAnimationController controller =
                                        AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_fall_down);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                item_recycler_view.setLayoutManager(linearLayoutManager);
                                item_recycler_view.setLayoutAnimation(controller);
                                item_recycler_view.getAdapter().notifyDataSetChanged();
                                item_recycler_view.scheduleLayoutAnimation();
                                itemAdapter.notifyDataSetChanged();

                            } else if (itemListRequestAndResponseModel.getStatus_code().equals(Constants.Failure)) {
                                Toast.makeText(getActivity(), itemListRequestAndResponseModel.getStatus_message(), Toast.LENGTH_LONG).show();
                            }
                        }else if (itemListRequestAndResponseModel.getStatusCode().equals(Constants.Failure))
                        {
                            gridImageList.clear();
                            itemAdapter.notifyDataSetChanged();
                            variety_recycler_view.setVisibility(View.GONE);
                            Toast.makeText(getActivity(),"No Item Found",Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ItemListRequestAndResponseModel> call, Throwable t) {

                }
            });

    }

    @Override
    public void onAddClick(int totalunitcount,ItemListRequestAndResponseModel.item_list item_lists) {
        txtBrodgeIcon.setVisibility(View.VISIBLE);

        if (itemList.size() != 0 ) {
            for (int i = 0; i < itemList.size(); i++) {
                ItemListRequestAndResponseModel.item_list list = itemList.get(i);

                if (list.getItem_id().equals(item_lists.getItem_id())) {
                    itemList.remove(list);

                    if (item_lists.getQty() != 0) {
                        if (!itemList.contains(item_lists))
                            itemList.add(item_lists);
                    }
                if (totalunitcount == 0)
                {
                    itemList.remove(list);
                }
                } else {
                    if (!itemList.contains(item_lists))
                        itemList.add(item_lists);
                }
            }
            if (item_lists.getQty() == 0)
            {
                itemList.remove(item_lists);
            }
        }else
        {
            itemList.add(item_lists);
        }
        /*if (itemList.contains(item_lists)) {
            itemList.remove(item_lists);
            itemList.add(item_lists);
            if (totalunitcount ==0)
            {
                itemList.remove(item_lists);
            }
        }else
        {
            itemList.add(item_lists);
        }*/
        itemModel.setListArrayList(itemList);
        txtBrodgeIcon.setText(String.valueOf(itemList.size()));
        if (totalunitcount == 0 && itemList.size() == 0)
        {
            itemList.remove(item_lists);
            itemModel.setListArrayList(itemList);
            txtBrodgeIcon.setText(String.valueOf(itemList.size()));
            txtBrodgeIcon.setVisibility(View.GONE);
        }
        changePrice();

    }

    public void changePrice()
    {
        double price = 0;
        if (itemModel.getListArrayList().size() !=  0) {
            for (int i = 0; i < itemModel.getListArrayList().size(); i++) {
                ItemListRequestAndResponseModel.item_list item_list = itemModel.getListArrayList().get(i);
                double getPrice = Double.parseDouble(item_list.getPrice());
                double qty = item_list.getQty();
                getPrice = getPrice * qty;
//            double priceGet = item_list.getQty() * item_list.getPrice();
                price = price + getPrice;
            }
//        txtTotalPrice.setText(String.valueOf(price));

            txtTotalPrice.setText("Total : " + String.valueOf(price));
        }
    }

    public void searchItemBasedOnCat(String cat_id)
    {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JSONObject getJsonObject = new JSONObject();
        try {
            getJsonObject.put("cat_id",cat_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<ItemListRequestAndResponseModel> getCatageoryList = apiInterface.getItemBasedOnCat(getJsonObject);
        getCatageoryList.enqueue(new Callback<ItemListRequestAndResponseModel>() {
            @Override
            public void onResponse(Call<ItemListRequestAndResponseModel> call, Response<ItemListRequestAndResponseModel> response) {
                if (response.isSuccessful())
                {
                    ItemListRequestAndResponseModel itemListRequestAndResponseModel = response.body();
                    if (itemListRequestAndResponseModel.getStatusCode().equals(Constants.Success))
                    {
                        if (itemListRequestAndResponseModel.getSub_cat_list() != null && itemListRequestAndResponseModel.getSub_cat_list().size() != 0)
                        {
                            ldtList.setVisibility(View.VISIBLE);
                            sub_cat_recycler_view.setVisibility(View.VISIBLE);
                            ArrayList<ItemListRequestAndResponseModel.cat_list>sub_cat_list = subCatList;
                            ArrayList<ItemListRequestAndResponseModel.cat_list>tempSubCatList = new ArrayList<>();


                            for (int i = 0 ; i < itemListRequestAndResponseModel.getSub_cat_list().size() ; i ++)
                            {
                                ItemListRequestAndResponseModel.sub_cat_list subCatList = itemListRequestAndResponseModel.getSub_cat_list().get(i);
                                for (int j = 0 ; j< sub_cat_list.size() ; j++)
                                {
                                    ItemListRequestAndResponseModel.cat_list cat_list = sub_cat_list.get(j);
                                    if (subCatList.getSub_cat_id().equals(cat_list.getSub_Cat_id()))
                                    {
                                        tempSubCatList.add(cat_list);
                                    }
                                }

                            }

                            subCatList.clear();
                            gridAdapter.clearView();
                            subCatList.addAll(tempSubCatList);
                            gridAdapter.notifyDataSetChanged();

                            if (itemListRequestAndResponseModel.getItem_list() != null)
                            {
                                ArrayList<ItemListRequestAndResponseModel.item_list>item_list = gridImageList;
                                ArrayList<ItemListRequestAndResponseModel.item_list>tempItemList = new ArrayList<>();

                                for (int i = 0 ; i < itemListRequestAndResponseModel.getItem_list().size() ; i ++)
                                {
                                    ItemListRequestAndResponseModel.item_list subCatList = itemListRequestAndResponseModel.getItem_list().get(i);
                                    for (int j = 0 ; j< item_list.size() ; j++)
                                    {
                                        ItemListRequestAndResponseModel.item_list cat_list = item_list.get(j);
                                        if (subCatList.getItem_id().equals(cat_list.getItem_id()))
                                        {
                                            tempItemList.add(cat_list);
                                        }
                                    }

                                }

                                gridImageList.clear();
                                gridImageList.addAll(tempItemList);
                                itemAdapter.notifyDataSetChanged();

                            }

                        }else{
                            ldtList.setVisibility(View.INVISIBLE);
                            sub_cat_recycler_view.setVisibility(View.INVISIBLE);
                            variety_recycler_view.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(),"No Item Found",Toast.LENGTH_LONG).show();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ItemListRequestAndResponseModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isNetworkConnected != isConnected) {
            if (isConnected) {
            } else {
            }
        }
        isNetworkConnected = isConnected;
    }

    public void addFavouriteApi(String getSpecificItemId, final int pos) {
        localItemId = getSpecificItemId;
        database = new Database(getActivity());
        // api call for the add favourite list
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", database.getCustomerId());
            jsonObject.put("item_id", getSpecificItemId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<LoginRequestAndResponse> ratingResponseModelCall = apiInterface.addFavouriteList(jsonObject);
        ratingResponseModelCall.enqueue(new Callback<LoginRequestAndResponse>() {
            @Override
            public void onResponse(Call<LoginRequestAndResponse> call, Response<LoginRequestAndResponse> response) {
                if (response.isSuccessful()){
                    if(response.body().getStatusCode().equals("1")){
                        Toast.makeText(getActivity(), "Favourite Added Successfully", Toast.LENGTH_SHORT).show();
                        ItemListRequestAndResponseModel.item_list item_list = gridImageList.get(pos);
                        item_list.setFavorite(true);
                        itemAdapter.notifyDataSetChanged();
                        itemAdapter.currentChangeFavouriteIcon();
                    }else {
                        Toast.makeText(getActivity(), "Favorite Already Exists.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
            @Override
            public void onFailure(Call<LoginRequestAndResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                progress_ratings.setVisibility(View.GONE);

            }
        });


    }


    private void getFavouriteListApi() {
        database = new Database(getActivity());
        favouriteListDetails_adapter = new ArrayList<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", database.getCustomerId());

        }catch (JSONException e){
            e.printStackTrace();
        }

        final Call<FavouriteListData> favouriteListDataCall = apiInterface.FavouriteListDetails(jsonObject);
        favouriteListDataCall.enqueue(new Callback<FavouriteListData>() {


            @Override
            public void onResponse(Call<FavouriteListData> call, Response<FavouriteListData> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus_code().equals("1")) {
                        FavouriteListData favouriteListData = response.body();
                        itemIdList = new ArrayList<>();
                        for (int i = 0; i < favouriteListData.getList().size(); i++) {
                            favouriteListData.getList().get(i).getSno();
                            favouriteListData.getList().get(i).getItem_id();
                            String itemId = favouriteListData.getList().get(i).getItem_id();
                            itemIdList.add(itemId);
                          /*  FavouriteListData.FavouriteListDetails favouriteListDetails = favouriteListData.getList().get(i);
                            favouriteListDetails_adapter.add(favouriteListDetails);*/
                        }

                      //  itemAdapter.currentChangeFavouriteIcon();


                        //itemAdapter.ItemAdapterCall();

                        getItemList();

                    }else if (response.body().getStatus_code().equals(Constants.Failure)) {
                        getItemList();
                    }
                }
            }


            @Override
            public void onFailure(Call<FavouriteListData> call, Throwable t) {

            }
        });



    }

}
