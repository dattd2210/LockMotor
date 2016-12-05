package com.lockmotor.implement.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tran Dinh Dat on 3/8/2016.
 */
@org.parceler.Parcel
public class InfoResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
