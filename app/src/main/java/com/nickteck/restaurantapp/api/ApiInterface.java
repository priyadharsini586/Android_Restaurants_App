package com.nickteck.restaurantapp.api;

import com.nickteck.restaurantapp.model.AddWhislist;
import com.nickteck.restaurantapp.model.HistoryModel;
import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;
import com.nickteck.restaurantapp.model.ItemModel;
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


    @POST("category_list.php")
    Call<ItemListRequestAndResponseModel> getCatagoryList();

    @POST("sub_category_list.php")
    Call<ItemListRequestAndResponseModel> getSubCatagoryList();

    @POST("veraity_list.php")
    Call<ItemListRequestAndResponseModel> getVarietyList();

    @FormUrlEncoded
    @POST("subcat_item_list.php")
    Call<ItemListRequestAndResponseModel> getItemList(@Field("x") JSONObject Itemobject);

    @FormUrlEncoded
    @POST("favourite_list.php")
    Call<AddWhislist> getWhishlist(@Field("x") JSONObject Itemobject);

    @POST("table_list.php")
    Call<TableModel> getTableData();

    @POST("item_list.php")
    Call<ItemListRequestAndResponseModel> getItemData();

    @FormUrlEncoded
    @POST("current_ip.php")
    Call<LoginRequestAndResponse> getIp(@Field("x") JSONObject object);

    @FormUrlEncoded
    @POST("customer_history.php")
    Call<HistoryModel> getHistoryDetails(@Field("x") JSONObject object);

    @FormUrlEncoded
    @POST("client_details_by_phone.php")
    Call<LoginRequestAndResponse> clientDetailsByNum(@Field("x") JSONObject object);

    @FormUrlEncoded
    @POST("cat_subcat_item_list.php")
    Call<ItemListRequestAndResponseModel> getItemBasedOnCat(@Field("x") JSONObject object);

}
