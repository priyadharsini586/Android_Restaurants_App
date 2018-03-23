package com.nickteck.restaurantapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 3/16/2018.
 */

public class Item {

  public String status_code,tot_items,success;
    public List<ItemList> item_list= new ArrayList<>();

    public List<ItemList> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ItemList> item_list) {
        this.item_list = item_list;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }
    public String getTot_items() {
        return tot_items;
    }

    public void setTot_items(String tot_items) {
        this.tot_items = tot_items;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
    public static class ItemList{
        public String item_id,item_name,description,price,image;
        public List<CatList> cat_list=new ArrayList<>();

        public List<CatList> getCat_list() {
            return cat_list;
        }

        public void setCat_list(List<CatList> cat_list) {
            this.cat_list = cat_list;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getItem_name() {
            return item_name;
        }

        public void setItem_name(String item_name) {
            this.item_name = item_name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
        public class CatList{
            public String cat_id,cat_name;

            public String getCat_id() {
                return cat_id;
            }

            public void setCat_id(String cat_id) {
                this.cat_id = cat_id;
            }

            public String getCat_name() {
                return cat_name;
            }

            public void setCat_name(String cat_name) {
                this.cat_name = cat_name;
            }
        }
    }


}
