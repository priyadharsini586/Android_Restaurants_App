package com.nickteck.restaurantapp.api;

import com.nickteck.restaurantapp.model.LoginRequestAndResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by admin on 3/7/2018.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("check_client_details.php")
     Call<LoginRequestAndResponse> getLoginResponse(@Field("x") JSONObject jsonObject);
}
