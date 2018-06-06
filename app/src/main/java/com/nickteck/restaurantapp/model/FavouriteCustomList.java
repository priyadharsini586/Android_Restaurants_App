package com.nickteck.restaurantapp.model;

/**
 * Created by admin on 6/4/2018.
 */

public class FavouriteCustomList {

    private String sno;
    private String item_id;
    // added for the manual values not from the server response
    private String description;
    private String image_url;
    private String item_price;
    private String name;

    public FavouriteCustomList(String sno, String item_id, String name, String description, String image_url, String item_price) {
        this.sno = sno;
        this.item_id = item_id;
        this.description = description;
        this.image_url = image_url;
        this.item_price = item_price;
        this.name = name;
    }

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
