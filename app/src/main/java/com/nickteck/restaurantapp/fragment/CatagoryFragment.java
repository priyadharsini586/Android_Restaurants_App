package com.nickteck.restaurantapp.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.nickteck.restaurantapp.Adapter.CatagoryAdapter;
import com.nickteck.restaurantapp.Adapter.GridAdapter;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.additional_class.AdditionalClass;
import com.nickteck.restaurantapp.additional_class.RecyclerTouchListener;
import com.nickteck.restaurantapp.api.ApiClient;
import com.nickteck.restaurantapp.api.ApiInterface;
import com.nickteck.restaurantapp.model.Constants;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.nickteck.restaurantapp.model.ItemModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nickteck.restaurantapp.model.Constants.CATEGORY_BASE_URL;

public class CatagoryFragment extends Fragment {
    View view;
    ApiInterface apiInterface;
    RecyclerView catagory;
    SwipeRefreshLayout mSwipeRefreshLayout;

    ArrayList<ItemListRequestAndResponseModel.list> catList;
    TextView txtBrodgeIcon;

    @SuppressLint("ResourceType")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cat, container, false);
        catagory=(RecyclerView)view.findViewById(R.id.recycler_view);
        catagory.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), catagory, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                ItemListRequestAndResponseModel.list list = catList.get(position);
                Bundle bundle=new Bundle();
                bundle.putString("listData", list.getId());
                ItemFragment itemFragment = new ItemFragment();
                itemFragment.setArguments(bundle);
                AdditionalClass.replaceFragment(itemFragment,Constants.ITEM_FRAGMENT,(AppCompatActivity)getActivity());
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


        mSwipeRefreshLayout.setRefreshing(true);
        getCategoryData();
        getCategoryData();
        return view;
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
        Call<ItemListRequestAndResponseModel> getCatageoryList = apiInterface.getCatagoryList(getJsonObject);
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
                        ArrayList getItemDetils = itemListRequestAndResponseModel.getList();
                        for (int i = 0; i < getItemDetils.size(); i++) {
                            ItemListRequestAndResponseModel.list  categoryList = (ItemListRequestAndResponseModel.list) getItemDetils.get(i);

                            categoryList.setName(categoryList.getName());
                            String url=CATEGORY_BASE_URL+categoryList.getImage();
                            categoryList.setImage(url);
                            catList.add(categoryList);
                        }
                        CatagoryAdapter gridAdapter=new CatagoryAdapter(getActivity(),catList);
                        catagory.setAdapter(gridAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
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

}
