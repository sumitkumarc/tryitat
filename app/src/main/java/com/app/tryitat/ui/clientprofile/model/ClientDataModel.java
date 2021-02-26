package com.app.tryitat.ui.clientprofile.model;

import java.util.HashMap;
import java.util.List;

public class ClientDataModel {
    String address;
    String approved;
    String email;
    String fcmToken;
    String lat;
    String lng;
    String objectId;
    String phone;
    String picture;
    String storeName;
    String storeSubName;
    String userType;
    String coverpicture;
    List<String> customers;
    List<String> followers;
    List<String> following;
    HashMap<String, String> rewards;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreSubName() {
        return storeSubName;
    }

    public void setStoreSubName(String storeSubName) {
        this.storeSubName = storeSubName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCoverpicture() {
        return coverpicture;
    }

    public void setCoverpicture(String coverpicture) {
        this.coverpicture = coverpicture;
    }

    public List<String> getCustomers() {
        return customers;
    }

    public void setCustomers(List<String> customers) {
        this.customers = customers;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    public HashMap<String, String> getRewards() {
        return rewards;
    }

    public void setRewards(HashMap<String, String> rewards) {
        this.rewards = rewards;
    }
}

