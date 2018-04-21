package com.nickteck.restaurantapp.interfaceFol;

import com.nickteck.restaurantapp.model.ItemListRequestAndResponseModel;

/**
 * Created by admin on 4/21/2018.
 */

public interface ItemListener {

    public void onAddClick(int totalunitcount, ItemListRequestAndResponseModel.item_list item_list);
}
