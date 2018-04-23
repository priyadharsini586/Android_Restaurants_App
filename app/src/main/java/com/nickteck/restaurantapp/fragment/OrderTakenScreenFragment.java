package com.nickteck.restaurantapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import com.nickteck.restaurantapp.Adapter.CatagoryAdapter;
import com.nickteck.restaurantapp.Adapter.ItemAdapter;
import com.nickteck.restaurantapp.Adapter.VarietyAdapter;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.additional_class.RecyclerTouchListener;
import com.nickteck.restaurantapp.api.ApiClient;
import com.nickteck.restaurantapp.api.ApiInterface;
import com.nickteck.restaurantapp.interfaceFol.ItemListener;
import com.nickteck.restaurantapp.model.Constants;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.nickteck.restaurantapp.model.ItemModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nickteck.restaurantapp.model.Constants.CATEGORY_BASE_URL;
import static com.nickteck.restaurantapp.model.Constants.ITEM_BASE_URL;


public class OrderTakenScreenFragment extends Fragment implements ItemListener{
    View view;
    ApiInterface apiInterface;
    RecyclerView catagory,variety_recycler_view,item_recycler_view;
    SwipeRefreshLayout mSwipeRefreshLayout;

    ArrayList<ItemListRequestAndResponseModel.cat_list> catList;
    ArrayList<ItemListRequestAndResponseModel.Variety_id_list> varietyIdLists;
    TextView txtBrodgeIcon;
    CatagoryAdapter gridAdapter;
    VarietyAdapter varietyAdapter;
    ItemAdapter itemAdapter;
    HashMap<String,ArrayList<ItemListRequestAndResponseModel.item_list>> getVarityList = new HashMap<>();
    private  ArrayList<ItemListRequestAndResponseModel.item_list> gridImageList=new ArrayList<>();
    public static ArrayList<ItemListRequestAndResponseModel.item_list> itemList = new ArrayList<>();
    ItemModel itemModel = ItemModel.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_taken_screen, container, false);
        catagory=(RecyclerView)view.findViewById(R.id.recycler_view);
        catagory.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), catagory, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                ItemListRequestAndResponseModel.cat_list list = catList.get(position);
                getVarityList = new HashMap<>();
                getVarietyData();
                getItemView(list.getCat_id());

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


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
                        getCategoryData();
                    }
                }, 3000);



            }
        });

        variety_recycler_view = (RecyclerView) view.findViewById(R.id.variety_recycler_view);
        variety_recycler_view.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), item_recycler_view, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (position != 0) {
                    ItemListRequestAndResponseModel.Variety_id_list varietyIdList = varietyIdLists.get(position);
                    Log.e("hashmap", String.valueOf(varietyIdList.getVariety_id()));
                    Log.e("hashmap", String.valueOf(getVarityList));

                    Log.e("hashmap", String.valueOf(getVarityList.get(varietyIdList.getVariety_id())));

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

                  Collection<ArrayList<ItemListRequestAndResponseModel.item_list>> itemLists = getVarityList.values();
                  Iterator iterator =itemLists.iterator();
                   /* while(iterator.hasNext()) {
                        ItemListRequestAndResponseModel.item_list element = (ItemListRequestAndResponseModel.item_list) iterator.next();
                        Log.e("item",element.getItem_id());

                    }*/


                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        item_recycler_view = (RecyclerView) view.findViewById(R.id.item_recycler_view);

        mSwipeRefreshLayout.setRefreshing(true);
        getCategoryData();
        getVarietyData();
        getItemList();
        return view;
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
                            for (int j = 0 ; j < itemModel.getListArrayList().size();j++)
                            {
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
                        itemAdapter=new ItemAdapter(gridImageList,getActivity());
                        itemAdapter.setListener(OrderTakenScreenFragment.this);
                        item_recycler_view.setAdapter(itemAdapter);
                        final LayoutAnimationController controller =
                                AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_fall_down);
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
                        ItemListRequestAndResponseModel.Variety_id_list model = new ItemListRequestAndResponseModel.Variety_id_list();
                        model.setVariety_name("All");
                        model.setVariety_id("0");
                        varietyIdLists.add(model);
                        for (int i = 0; i < getItemDetils.size(); i++) {

                            ItemListRequestAndResponseModel.Variety_id_list categoryList = (ItemListRequestAndResponseModel.Variety_id_list) getItemDetils.get(i);
                            varietyIdLists.add(categoryList);

                            Log.e("variety",categoryList.getVariety_name());
                        }


                        varietyAdapter=new VarietyAdapter(getActivity(),varietyIdLists);

                        variety_recycler_view.setAdapter(varietyAdapter);
//                        catagory.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL));
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                        variety_recycler_view.setLayoutManager(linearLayoutManager);
                        final LayoutAnimationController controller =
                                AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_fall_down);

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

    public void getCategoryData()
    {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JSONObject getJsonObject = new JSONObject();
        try {
            getJsonObject.put("from",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                            categoryList.setImage(url);
                            catList.add(categoryList);

                        }
                         gridAdapter=new CatagoryAdapter(getActivity(),catList);

                        catagory.setAdapter(gridAdapter);
//                        catagory.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL));
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                        catagory.setLayoutManager(linearLayoutManager);
                        final LayoutAnimationController controller =
                                AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_fall_down);

                        catagory.setLayoutAnimation(controller);
                        catagory.getAdapter().notifyDataSetChanged();
                        catagory.scheduleLayoutAnimation();
                        gridAdapter.notifyDataSetChanged();


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
                getJsonObject.put("cat_id",catId);
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
                                for (int j = 0 ; j < itemModel.getListArrayList().size();j++)
                                {
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

                                ArrayList<ItemListRequestAndResponseModel.Variety_id_list> varityList= items.getVariety_list();
                                ArrayList<ItemListRequestAndResponseModel.item_list> itemVarityList = new ArrayList<>();
                                if (varityList.size() !=0) {
                                    ArrayList varityId = new ArrayList();
                                    for (int j = 0; j < varityList.size(); j++) {
                                        ItemListRequestAndResponseModel.Variety_id_list variety_id_list = varityList.get(j);
                                        varityId.add(variety_id_list.getVarietyid());
                                        itemVarityList.add(items);
                                        if (getVarityList.containsKey(variety_id_list.getVarietyid())) {
                                            ArrayList<ItemListRequestAndResponseModel.item_list> list = getVarityList.get(variety_id_list.getVarietyid());
                                            for (int  k= 0 ; k< list.size() ; k ++)
                                            {
                                                itemVarityList.add(list.get(k));
                                            }
                                            getVarityList.put(variety_id_list.getVarietyid(), itemVarityList);
                                            itemVarityList = new ArrayList<>();
                                        }else
                                        {
                                            getVarityList.put(variety_id_list.getVarietyid(), itemVarityList);
                                            itemVarityList = new ArrayList<>();
                                        }
                                    }

                                }
                            }
                            itemAdapter=new ItemAdapter(gridImageList,getActivity());
                            itemAdapter.setListener(OrderTakenScreenFragment.this);
                            item_recycler_view.setAdapter(itemAdapter);
                            final LayoutAnimationController controller =
                                    AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_fall_down);
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

    @Override
    public void onAddClick(int totalunitcount,ItemListRequestAndResponseModel.item_list item_lists) {
        txtBrodgeIcon.setVisibility(View.VISIBLE);

        if (itemList.size() != 0) {
            for (int i = 0; i < itemList.size(); i++) {
                ItemListRequestAndResponseModel.item_list list = itemList.get(i);

                if (list.getItem_id().equals(item_lists.getItem_id())) {
                    itemList.remove(list);

                    if (item_lists.getQty() != 0) {
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

    }
}
