package com.nickteck.restaurantapp.model;

/**
 * Created by admin on 5/31/2018.
 */

public class RatingResponseModel {

    private String status_code;
    private String status_message;
    private String success;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
