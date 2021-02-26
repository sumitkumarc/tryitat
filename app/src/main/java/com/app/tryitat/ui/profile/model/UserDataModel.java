package com.app.tryitat.ui.profile.model;

public class UserDataModel {
    String userId;
    String name;
    String picture;
    String coverpicture;
    String email;
    String fcmToken;
    String gender;
    String mobilePhone;
    int points;

    public UserDataModel() {
    }

    public UserDataModel(String userId, String name, String picture, String coverpicture, String email, String fcmToken, String gender, String mobilePhone, int points) {
        this.userId = userId;
        this.name = name;
        this.picture = picture;
        this.coverpicture = coverpicture;
        this.email = email;
        this.fcmToken = fcmToken;
        this.gender = gender;
        this.mobilePhone = mobilePhone;
        this.points = points;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCoverpicture() {
        return coverpicture;
    }

    public void setCoverpicture(String coverpicture) {
        this.coverpicture = coverpicture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
