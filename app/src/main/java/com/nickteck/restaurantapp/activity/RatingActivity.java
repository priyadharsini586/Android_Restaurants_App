package com.nickteck.restaurantapp.activity;

import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.nickteck.restaurantapp.Db.Database;
import com.nickteck.restaurantapp.R;
import com.nickteck.restaurantapp.additional_class.AdditionalClass;
import com.nickteck.restaurantapp.api.ApiClient;
import com.nickteck.restaurantapp.api.ApiInterface;
import com.nickteck.restaurantapp.model.LoginRequestAndResponse;
import com.nickteck.restaurantapp.model.RatingResponseModel;
import com.nickteck.restaurantapp.network.ConnectivityReceiver;
import com.nickteck.restaurantapp.network.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    private EditText rating_suggestion;
    private Button rating_submit_button;
    private String getComment;
    ApiInterface apiInterface;
    Database database;
    private RatingBar rating_q1;
    private RatingBar rating_q2;
    private RatingBar rating_q3;
    private RatingBar rating_q4;
    private String ratingValue1;
    private String ratingValue2;
    private String ratingValue3;
    private String ratingValue4;
    private ProgressBar progress_ratings;
    boolean isNetworkConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        //code that displays the content in full screen mode
        MyApplication.getInstance().setConnectivityListener(this);
        if (AdditionalClass.isNetworkAvailable(this)) {
            isNetworkConnected = true;
        }else {
            isNetworkConnected = false;
        }

        init();
        onclickListener();
    }

    private void init() {
        database = new Database(RatingActivity.this);
        rating_suggestion = (EditText) findViewById(R.id.rating_suggestion);
        rating_submit_button = (Button) findViewById(R.id.rating_submit_button);
        rating_q1 = (RatingBar) findViewById(R.id.rating_q1);
        rating_q2 = (RatingBar) findViewById(R.id.rating_q2);
        rating_q3 = (RatingBar) findViewById(R.id.rating_q3);
        rating_q4 = (RatingBar) findViewById(R.id.rating_q4);
        progress_ratings = (ProgressBar)findViewById(R.id.progress_ratings);
        progress_ratings.setVisibility(View.GONE);
       // rating_q5 = (RatingBar) findViewById(R.id.rating_q5);
    }



    private void onclickListener() {
        rating_q1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingValue1 = String.valueOf(rating_q1.getRating());
            }
        });

        rating_q2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingValue2 = String.valueOf(rating_q2.getRating());
            }
        });

        rating_q3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingValue3 = String.valueOf(rating_q3.getRating());
            }
        });

        rating_q4.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingValue4 = String.valueOf(rating_q4.getRating());

            }
        });
        rating_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        if (isNetworkConnected) {
                            if(checkForEmptyData()){
                                progress_ratings.setVisibility(View.VISIBLE);
                                getAllValues();
                                sendApiCall();
                            }

                        }else {
                            AdditionalClass.showSnackBar(RatingActivity.this);
                        }
            }
        });

    }
    private void getAllValues() {
        if(getComment == null){
            getComment = "";
        }if(ratingValue1 == null){
            ratingValue1 = "";
        }if(ratingValue2 == null){
            ratingValue2 = "";
        }if(ratingValue3 == null){
            ratingValue3 = "";
        }if(ratingValue4 == null){
            ratingValue4 = "";
        }
    }

    private boolean checkForEmptyData() {
        getComment = rating_suggestion.getText().toString();

        if((getComment.toString().equals("")||(getComment == null)) &&(ratingValue1 == null) &&(ratingValue2 == null)
                && (ratingValue3 == null) &&(ratingValue4 == null)){
            Toast.makeText(this, "Please give Ratings or Comments", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void sendApiCall() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id",database.getCustomerId());
            jsonObject.put("q1", ratingValue1);
            jsonObject.put("q2", ratingValue2);
            jsonObject.put("q3", ratingValue3);
            jsonObject.put("q4", ratingValue4);
            jsonObject.put("q5", "");
            jsonObject.put("suggestion",getComment);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<RatingResponseModel> ratingResponseModelCall = apiInterface.getRatingResponse(jsonObject);
        ratingResponseModelCall.enqueue(new Callback<RatingResponseModel>() {

            @Override
            public void onResponse(Call<RatingResponseModel> call, Response<RatingResponseModel> response) {
                if (response.isSuccessful()){
                    Toast.makeText(RatingActivity.this, "Rated Successfully", Toast.LENGTH_SHORT).show();
                    progress_ratings.setVisibility(View.GONE);
                    finish();
                }


            }

            @Override
            public void onFailure(Call<RatingResponseModel> call, Throwable t) {
                Toast.makeText(RatingActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                progress_ratings.setVisibility(View.GONE);

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

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }
}
