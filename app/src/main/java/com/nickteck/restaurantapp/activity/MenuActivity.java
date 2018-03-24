package com.nickteck.restaurantapp.activity;

;
import android.os.Build;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.nickteck.restaurantapp.Adapter.GridAdapter;
import com.nickteck.restaurantapp.Adapter.ViewPagerAdapter;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.api.ApiClient;
import com.nickteck.restaurantapp.api.ApiInterface;
import com.nickteck.restaurantapp.model.Constants;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ItemListRequestAndResponseModel> imageModelArrayList;
    ArrayList<ItemListRequestAndResponseModel.list> gridImageList;
    private int [] sliderList = {R.drawable.cook2,R.drawable.cook3,R.drawable.cook4,R.drawable.cook5};
    GridView simpleGrid;
    ProgressBar progressCategoryList;
    ApiInterface apiInterface;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        simpleGrid = (GridView) findViewById(R.id.simpleGridView);
        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();
        progressCategoryList = (ProgressBar) findViewById(R.id.progressCategoryList);
        progressCategoryList.setVisibility(View.VISIBLE);
        init();
        getCategoryData();

    }
    private ArrayList<ItemListRequestAndResponseModel> populateList(){

        ArrayList<ItemListRequestAndResponseModel> list = new ArrayList<>();

        for(int i = 0; i <sliderList.length; i++){
            ItemListRequestAndResponseModel imageModel = new ItemListRequestAndResponseModel();
            imageModel.setImage_drawable(sliderList[i]);
            list.add(imageModel);
        }

        return list;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
      private void animatePage(){
             //Transition transition= TransitionInflater.from(this).inflateTransition(R.transition.explode);
              Explode transition=new Explode();
              transition.setDuration(getResources().getInteger(R.integer.anim_duration));
             getWindow().setEnterTransition(transition);


            }
    private void init() {

        mPager = (ViewPager) findViewById(R.id.viewPager);
        mPager.setAdapter(new ViewPagerAdapter(MenuActivity.this,imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);


        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

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
        Call<ItemListRequestAndResponseModel> getCatageoryList = apiInterface.getCatagoryList(getJsonObject);
        getCatageoryList.enqueue(new Callback<ItemListRequestAndResponseModel>() {
            @Override
            public void onResponse(Call<ItemListRequestAndResponseModel> call, Response<ItemListRequestAndResponseModel> response) {
                if (response.isSuccessful())
                {
                    progressCategoryList.setVisibility(View.GONE);
                    ItemListRequestAndResponseModel itemListRequestAndResponseModel = response.body();
                    if (itemListRequestAndResponseModel.getStatus_code().equals(Constants.Success))
                    {
                        gridImageList = new ArrayList<>();
                        ArrayList getItemDetils = itemListRequestAndResponseModel.getList();
                        for (int i = 0; i < getItemDetils.size(); i++) {
                            ItemListRequestAndResponseModel.list  categoryList = (ItemListRequestAndResponseModel.list) getItemDetils.get(i);
                            gridImageList.add(categoryList);
                        }
                        GridAdapter gridAdapter=new GridAdapter(getApplicationContext(),gridImageList);
                        simpleGrid.setAdapter(gridAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ItemListRequestAndResponseModel> call, Throwable t) {

            }
        });
    }
}
