package com.nickteck.restaurantapp.model;

import java.util.ArrayList;

/**
 * Created by admin on 4/9/2018.
 */

public class TableModel {
    private String Status_code,Success;
    public ArrayList<TableModel.list> list = new ArrayList<>();

    public ArrayList<TableModel.list> getList() {
        return list;
    }

    public void setList(ArrayList<TableModel.list> list) {
        this.list = list;
    }

    public String getStatus_code() {
        return Status_code;
    }

    public void setStatus_code(String status_code) {
        Status_code = status_code;
    }

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        Success = success;
    }
public class list{
public String id,name,active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
}
