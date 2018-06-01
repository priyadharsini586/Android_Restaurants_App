package com.nickteck.restaurantapp.additional_class;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.gson.Gson;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.activity.LoginActivity;
import com.nickteck.restaurantapp.model.AddWhislist;
import com.nickteck.restaurantapp.network.MyApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 3/7/2018.
 */

public class AdditionalClass {
    public static final String PREFS_NAME = "MY_APP";
    public static final String FAVORITES = "code_Favorite";
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static void showSnackBar(Activity context) {
        TSnackbar snackbar = TSnackbar
                .make(context.findViewById(android.R.id.content), "Network Not Connected", TSnackbar.LENGTH_LONG)
                .setAction("", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("Action Button", "onClick triggered");

                    }
                });
        snackbar.setActionTextColor(Color.parseColor("#00628f"));

//        snackbar.addIcon(R.mipmap.ic_core, 200); <<-- replace me!
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor("#f48220"));
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        snackbar.setDuration(60000);
        snackbar.show();

    }

    public static void showSnackBar1(View view,String msg) {
        TSnackbar snackbar = TSnackbar.make(view, msg, TSnackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();

        ViewGroup.LayoutParams params = snackbarView.getLayoutParams();
        params.width = view.getWidth();
        snackbarView.setLayoutParams(params);

        snackbar.setActionTextColor(Color.parseColor("#00628f"));
        snackbarView.setBackgroundColor(Color.parseColor("#f48220"));
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.BLACK);
        snackbar.setDuration(5000);
        snackbar.show();
    }

    public static void replaceFragment(Fragment fragment, String fragmentTag,AppCompatActivity context) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.replace(R.id.rldMainContainer, fragment, fragmentTag);
        fragmentTransaction.commit();

    }

    public static boolean isInteger(String s) {
        boolean isValidInteger = false;
        try
        {
            Integer.parseInt(s);

            // s is a valid integer

            isValidInteger = true;
        }
        catch (NumberFormatException ex)
        {
            // s is not an integer
        }

        return isValidInteger;
    }

    public static void hideKeyboard(AppCompatActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
