package com.lockmotor.implement.models;

/**
 * Created by trandinhdat on 12/3/16.
 */

public class InfoRequest {
    String phone_number;

    public InfoRequest(String phone_number){
        this.phone_number = phone_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
