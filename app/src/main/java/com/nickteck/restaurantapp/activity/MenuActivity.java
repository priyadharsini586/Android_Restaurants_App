package com.nickteck.restaurantapp.activity;

;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nickteck.restaurantapp.Adapter.GridAdapter;
import com.nickteck.restaurantapp.Adapter.ViewPagerAdapter;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.model.ImageModel;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class MenuActivity extends AppCompatActivity {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;
    private int [] myImageList = {R.drawable.cook2,R.drawable.cook3,R.drawable.cook4,R.drawable.cook5};
    GridView simpleGrid;

    int cook[] = {R.drawable.cook1, R.drawable.cook2, R.drawable.cook3, R.drawable.cook4,
            R.drawable.cook5, R.drawable.cook6, R.drawable.cook7, R.drawable.cook8, R.drawable.cook9};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        simpleGrid = (GridView) findViewById(R.id.simpleGridView);
        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();
        init();
        GridAdapter gridAdapter=new GridAdapter(getApplicationContext(),cook);
        simpleGrid.setAdapter(gridAdapter);
    }
    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i <myImageList.length; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }

        return list;
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

        // Pager listener over indicator
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

}
