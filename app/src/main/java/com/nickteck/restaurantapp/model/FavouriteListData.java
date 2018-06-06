package com.nickteck.restaurantapp.model;

import java.util.ArrayList;

/**
 * Created by admin on 6/2/2018.
 */

public class FavouriteListData {

    private String Status_code;
    private ArrayList<FavouriteListDetails> list;

    public String getStatus_code() {
        return Status_code;
    }

    public void setStatus_code(String status_code) {
        Status_code = status_code;
    }

    public ArrayList<FavouriteListDetails> getList() {
        return list;
    }

    public void setList(ArrayList<FavouriteListDetails> list) {
        this.list = list;
    }

    public class FavouriteListDetails{
        private String sno;
        private String item_id;
        // added for the manual values not from the server response
        private String description;
        private String image_url;
        private String item_price;
        private String name;


        public String getSno() {
            return sno;
        }

        public void setSno(String sno) {
            this.sno = sno;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getItem_price() {
            return item_price;
        }

        public void setItem_price(String item_price) {
            this.item_price = item_price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
