package com.nickteck.restaurantapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nickteck.restaurantapp.Adapter.OrderAdapter;
import com.nickteck.restaurantapp.Db.Database;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.additional_class.AdditionalClass;
import com.nickteck.restaurantapp.fragment.ContentFragment;
import com.nickteck.restaurantapp.fragment.FavouriteFragment;
import com.nickteck.restaurantapp.fragment.HistoryFragment;
import com.nickteck.restaurantapp.fragment.MyOrdersFragment;
import com.nickteck.restaurantapp.fragment.OrderFragment;
import com.nickteck.restaurantapp.fragment.OrderTakenScreenFragment;
import com.nickteck.restaurantapp.model.Constants;
import com.nickteck.restaurantapp.network.ConnectivityReceiver;
import com.nickteck.restaurantapp.network.MyApplication;

public class MenuNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,ConnectivityReceiver.ConnectivityReceiverListener  {

    TextView txtHomeToolBar;
    FrameLayout layBadge;
    Database database;
    boolean isNetworkConnected;
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_navigation);
        // checking internet connection is exist or not
        MyApplication.getInstance().setConnectivityListener(this);
        if (AdditionalClass.isNetworkAvailable(this)) {
            isNetworkConnected = true;
        }else {
            isNetworkConnected = false;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.snackbar_id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ContentFragment contentFragment = new ContentFragment();
        AdditionalClass.replaceFragment(contentFragment, Constants.CONTENTFRAGMENT,MenuNavigationActivity.this);

        txtHomeToolBar = (TextView) findViewById(R.id.txtHomeToolBar);
        txtHomeToolBar.setText("Check");
        layBadge = (FrameLayout) findViewById(R.id.layBadge);


        layBadge.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (AdditionalClass.isNetworkAvailable(this)) {
            isNetworkConnected = true;
        }else {
            isNetworkConnected = false;
        }

        if(isNetworkConnected){
            if (id == R.id.nav_menu) {
                OrderTakenScreenFragment catagoryFragment = new OrderTakenScreenFragment();
                AdditionalClass.replaceFragment(catagoryFragment,Constants.ORDER_TAKEN_FRAGMENT,MenuNavigationActivity.this);

            } else if (id == R.id.nav_gallery) {
                FavouriteFragment favouriteFragment=new FavouriteFragment();
                AdditionalClass.replaceFragment(favouriteFragment,Constants.FAVOURITE_FRAGMENT,MenuNavigationActivity.this);


            } else if (id == R.id.nav_my_orders) {
                OrderFragment orderFragment = new OrderFragment();
                AdditionalClass.replaceFragment(orderFragment,Constants.ORDER_FRAGMENT,MenuNavigationActivity.this);
            } else if (id == R.id.nav_manage) {

            } else if (id == R.id.nav_share) {

            } else if (id == R.id.nav_history) {
                HistoryFragment myOrdersFragment = new HistoryFragment();
                AdditionalClass.replaceFragment(myOrdersFragment,Constants.HISTORY_FRAGMENT,MenuNavigationActivity.this);
            }else if(id == R.id.nav_about_us){
                Intent intent = new Intent(this,RatingActivity.class);
                startActivity(intent);
            }else if(id == R.id.nav_logout){
                SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
                settings.edit().clear().commit();
                database = new Database(getApplicationContext());
                database.deleteAll();
                finish();
                goToLoginActivity();
            }

        }else {
            AdditionalClass.showSnackBar1(coordinatorLayout,"Network Not Connected");
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this,TableActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.layBadge:
                MyOrdersFragment myOrdersFragment = new MyOrdersFragment();
                AdditionalClass.replaceFragment(myOrdersFragment,Constants.MY_ORDERS_FRAGMENT,MenuNavigationActivity.this);
                break;
        }
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

  /*  public void replaceFragment(Fragment fragment, String fragmentTag) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.replace(R.id.rldMainContainer, fragment, fragmentTag);
        fragmentTransaction.commit();

    }*/
  @Override
  public boolean onKeyUp(int keyCode, KeyEvent event) {
      boolean back = false;
      if(keyCode == KeyEvent.KEYCODE_BACK){
          back = true;
          backStack();
      }
      return back;

  }

   private void backStack(){
        if(getSupportFragmentManager().getBackStackEntryCount()>1){
            getSupportFragmentManager().popBackStack();
        }else
        if(getSupportFragmentManager().getBackStackEntryCount()==1){
            this.finish();
        }
    }
}
