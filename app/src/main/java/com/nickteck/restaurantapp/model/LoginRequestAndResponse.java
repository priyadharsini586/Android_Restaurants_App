package com.nickteck.restaurantapp.model;

/**
 * Created by admin on 3/7/2018.
 */

public class LoginRequestAndResponse {

    public String status_code,status_message,no_of_visit,success;

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

    public String getNo_of_visit() {
        return no_of_visit;
    }

    public void setNo_of_visit(String no_of_visit) {
        this.no_of_visit = no_of_visit;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
