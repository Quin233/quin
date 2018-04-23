package com.schrondinger.quin.bean;

import java.io.Serializable;

public class User implements Serializable {
    private String uid;
    private String userName;
    private String userPhone;
    private String userCountry;
    private String userCountryCode;
    private String userHeadPhoto;
    private String userType;
    private String gender;
    private String info;
    private String userPwd;
    private String userGestruePwd;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getUserCountryCode() {
        return userCountryCode;
    }

    public void setUserCountryCode(String userCountryCode) {
        this.userCountryCode = userCountryCode;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserGestruePwd() {
        return userGestruePwd;
    }

    public void setUserGestruePwd(String userGestruePwd) {
        this.userGestruePwd = userGestruePwd;
    }

    public String getUserHeadPhoto() {
        return userHeadPhoto;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserType() {
        return userType;
    }
}
