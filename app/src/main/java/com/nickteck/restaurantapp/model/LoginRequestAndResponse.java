package com.nickteck.restaurantapp.model;

/**
 * Created by admin on 3/7/2018.
 */

public class LoginRequestAndResponse {

    public String status_code,status_message,no_of_visit,success,customer_id,Status_code,current_ip;

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

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

    public String getCurrent_ip() {
        return current_ip;
    }

    public void setCurrent_ip(String current_ip) {
        this.current_ip = current_ip;
    }

    public String getStatusCode() {
        return Status_code;
    }

    public void setStatusCode(String status_code) {
        this.Status_code = status_code;
    }
}
