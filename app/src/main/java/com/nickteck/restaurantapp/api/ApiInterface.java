package com.nickteck.restaurantapp.api;

import com.nickteck.restaurantapp.model.AddWhislist;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.nickteck.restaurantapp.model.LoginRequestAndResponse;
import com.nickteck.restaurantapp.model.TableModel;

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

    @FormUrlEncoded
    @POST("category_view.php")
    Call<ItemListRequestAndResponseModel> getCatagoryList(@Field("x") JSONObject Itemobject);

    @FormUrlEncoded
    @POST("cat_item_list.php")
    Call<ItemListRequestAndResponseModel> getItemList(@Field("x") JSONObject Itemobject);

    @FormUrlEncoded
    @POST("favourite_list.php")
    Call<AddWhislist> getWhishlist(@Field("x") JSONObject Itemobject);

    @POST("table_list.php")
    Call<TableModel> getTableData();

}
