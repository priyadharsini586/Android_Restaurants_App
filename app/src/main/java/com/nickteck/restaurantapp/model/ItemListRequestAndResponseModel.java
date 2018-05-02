package com.nickteck.restaurantapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 3/23/2018.
 */

public class ItemListRequestAndResponseModel {

    String status_code,itemId,itemName,itemDescription,itemImage,itemPrice,itemShortCode,id,name,status,value,combine,status_message,comboList;
    int tot_items,tot_taxes;
    public ArrayList<item_list> item_list = new ArrayList<>();
    public ArrayList<sub_cat_list> sub_cat_list = new ArrayList<>();
    public ArrayList<cat_list> cat_list = new ArrayList<>();
    public ArrayList<Variety_id_list> Variety_id_list = new ArrayList<>();
    ArrayList<list>list = new ArrayList<>();
    String itemCategoryList,Status_code,Success ;
    int image_drawable;

    public ArrayList<ItemListRequestAndResponseModel.sub_cat_list> getSub_cat_list() {
        return sub_cat_list;
    }

    public void setSub_cat_list(ArrayList<ItemListRequestAndResponseModel.sub_cat_list> sub_cat_list) {
        this.sub_cat_list = sub_cat_list;
    }

    public ArrayList<ItemListRequestAndResponseModel.Variety_id_list> getVariety_id_list() {
        return Variety_id_list;
    }

    public void setVariety_id_list(ArrayList<ItemListRequestAndResponseModel.Variety_id_list> variety_id_list) {
        Variety_id_list = variety_id_list;
    }

    public ArrayList<ItemListRequestAndResponseModel.cat_list> getCat_list() {
        return cat_list;
    }

    public void setCat_list(ArrayList<ItemListRequestAndResponseModel.cat_list> cat_list) {
        this.cat_list = cat_list;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public int getImage_drawable() {
        return image_drawable;
    }

    public void setImage_drawable(int image_drawable) {
        this.image_drawable = image_drawable;
    }

    public String getComboList() {
        return comboList;
    }

    public void setComboList(String comboList) {
        this.comboList = comboList;
    }

    public String getCombine() {
        return combine;
    }

    public void setCombine(String combine) {
        this.combine = combine;
    }

    public ArrayList<list> getTax_list() {
        return list;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ArrayList<ItemListRequestAndResponseModel.list> getList() {
        return list;
    }

    public void setList(ArrayList<ItemListRequestAndResponseModel.list> list) {
        this.list = list;
    }

    public void setTax_list(ArrayList<list> tax_list) {
        this.list = tax_list;
    }

    public int getTot_taxes() {
        return tot_taxes;
    }

    public void setTot_taxes(int tot_taxes) {
        this.tot_taxes = tot_taxes;
    }

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        Success = success;
    }

    public void setStatusCode(String code)
    {
        this.Status_code = code;

    }


    public String getStatusCode() {
        return Status_code;
    }


    public String getItemShortCode() {
        return itemShortCode;
    }

    public void setItemShortCode(String itemShortCode) {
        this.itemShortCode = itemShortCode;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public ArrayList<ItemListRequestAndResponseModel.item_list> getItem_list() {
        return item_list;
    }

    public void setItem_list(ArrayList<ItemListRequestAndResponseModel.item_list> item_list) {
        this.item_list = item_list;
    }

    public String getItemCategoryList() {
        return itemCategoryList;
    }

    public void setItemCategoryList(String itemCategoryList) {
        this.itemCategoryList = itemCategoryList;
    }

    public int getTot_items() {
        return tot_items;
    }

    public void setTot_items(int tot_items) {
        this.tot_items = tot_items;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public ArrayList<item_list> getItem_lists() {
        return item_list;
    }

    public void setItem_lists(ArrayList<item_list> item_lists) {
        this.item_list = item_lists;
    }

    public static class item_list
    {
        String item_id,item_name,description,price,image,short_code,favourite;
        List<cat_list> cat_list;
        int qty;
        ArrayList<Variety_id_list>variety_list  = new ArrayList();
        String notes,date;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public ArrayList<ItemListRequestAndResponseModel.Variety_id_list> getVariety_list() {
            return variety_list;
        }

        public void setVariety_list(ArrayList<ItemListRequestAndResponseModel.Variety_id_list> variety_list) {
            this.variety_list = variety_list;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public String getFavourite() {
            return favourite;
        }

        public void setFavourite(String favourite) {
            this.favourite = favourite;
        }

        public String getShort_code() {
            return short_code;
        }

        public void setShort_code(String short_code) {
            this.short_code = short_code;
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

        public List<cat_list> getCat_list() {
            return cat_list;
        }

        public void setCat_list(List<cat_list> cat_list) {
            this.cat_list = cat_list;
        }
    }

    public class cat_list
    {
        String Cat_id,Cat_name,Image,Sub_Cat_name,Sub_Cat_id;

        public String getSub_Cat_id() {
            return Sub_Cat_id;
        }

        public void setSub_Cat_id(String sub_Cat_id) {
            Sub_Cat_id = sub_Cat_id;
        }

        public String getCat_id() {
            return Cat_id;
        }

        public void setCat_id(String cat_id) {
            Cat_id = cat_id;
        }

        public String getCat_name() {
            return Cat_name;
        }

        public void setCat_name(String cat_name) {
            Cat_name = cat_name;
        }

        public String getImage() {
            return Image;
        }

        public void setImage(String image) {
            Image = image;
        }

        public String getSub_Cat_name() {
            return Sub_Cat_name;
        }

        public void setSub_Cat_name(String sub_Cat_name) {
            Sub_Cat_name = sub_Cat_name;
        }
    }

    public class list{
        String id,name,value,comp1,comp2,active,item_list, tag_line, image;;

        public String getTag_line() {
            return tag_line;
        }

        public void setTag_line(String tag_line) {
            this.tag_line = tag_line;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getItem_list() {
            return item_list;
        }

        public void setItem_list(String item_list) {
            this.item_list = item_list;
        }

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

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getComp1() {
            return comp1;
        }

        public void setComp1(String comp1) {
            this.comp1 = comp1;
        }

        public String getComp2() {
            return comp2;
        }

        public void setComp2(String comp2) {
            this.comp2 = comp2;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }
    }


    public static class Variety_id_list
    {

        public Variety_id_list()
        {}
        String Variety_id,Variety_name,variety_id;

        public String getVariety_id() {
            return Variety_id;
        }

        public void setVariety_id(String variety_id) {
            Variety_id = variety_id;
        }

        public String getVariety_name() {
            return Variety_name;
        }

        public void setVariety_name(String variety_name) {
            Variety_name = variety_name;
        }

        public String getVarietyid() {
            return variety_id;
        }

        public void setVarietyid(String variety_id) {
            variety_id = variety_id;
        }
    }
    public class sub_cat_list{
        String sub_cat_id,sub_cat_name;

        public String getSub_cat_id() {
            return sub_cat_id;
        }

        public void setSub_cat_id(String sub_cat_id) {
            this.sub_cat_id = sub_cat_id;
        }

        public String getSub_cat_name() {
            return sub_cat_name;
        }

        public void setSub_cat_name(String sub_cat_name) {
            this.sub_cat_name = sub_cat_name;
        }
    }
}
