package com.nickteck.restaurantapp.model;

import java.util.ArrayList;

/**
 * Created by admin on 4/25/2018.
 */

public class HistoryModel {
    private static final HistoryModel ourInstance = new HistoryModel();
    String Status_code;
    ArrayList<item_list> item_list = new ArrayList<>();

    public String getStatus_code() {
        return Status_code;
    }

    public void setStatus_code(String status_code) {
        Status_code = status_code;
    }

    public ArrayList<HistoryModel.item_list> getItem_list() {
        return item_list;
    }

    public void setItem_list(ArrayList<HistoryModel.item_list> item_list) {
        this.item_list = item_list;
    }

    public static HistoryModel getInstance() {
        return ourInstance;
    }

    private HistoryModel() {
    }

    public class item_list
    {
        String date;
        ArrayList<items>items = new ArrayList<>();

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public ArrayList<HistoryModel.items> getItems() {
            return items;
        }

        public void setItems(ArrayList<HistoryModel.items> items) {
            this.items = items;
        }
    }

    public static class items
    {
        String item_id,date;

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
