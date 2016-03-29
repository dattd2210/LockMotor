package com.lockmotor.Features.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Tran Dinh Dat on 3/8/2016.
 */
public class User implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("email")
    private String email;
    @SerializedName("date_joined")
    private String createDate;
    @SerializedName("birthday")
    private String birthDay;
    @SerializedName("tel")
    private String tel;
    @SerializedName("gender")
    private int gender;
    @SerializedName("change_password")
    private boolean changePassord;
    @SerializedName("token")
    private String token;

    public String getUserName() {
        if (lastName == null && firstName == null)
            return "";
        if (lastName == null || lastName.isEmpty())
            return getFirstName();
        return getFirstName() + " " + getLastName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return (firstName == null) ? "" : firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return (lastName == null) ? "" : lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public boolean isChangePassord() {
        return changePassord;
    }

    public void setChangePassord(boolean changePassord) {
        this.changePassord = changePassord;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
