package com.nickteck.restaurantapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.activity.MenuActivity;
import com.nickteck.restaurantapp.model.ImageModel;

import java.util.ArrayList;

/**
 * Created by admin on 3/22/2018.
 */


public class ViewPagerAdapter extends PagerAdapter {
    private ArrayList<ImageModel> images;
    private Context context;
    private LayoutInflater layoutInflater;


    public ViewPagerAdapter(Context context, ArrayList<ImageModel> images) {
        this.images = images;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.image_layout, null);
        assert view != null;
        final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageResource(images.get(position).getImage_drawable());


        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}